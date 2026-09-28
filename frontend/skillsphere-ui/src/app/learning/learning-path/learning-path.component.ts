import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LearningService } from '../learning.service';
import { KeycloakAuthService } from '../../services/keycloak-auth.service';
import { HttpClient } from '@angular/common/http';
import { EmployeeService } from '../../services/employee.service';

interface Course {
  id?: string | number;
  courseId?: string | number;
  title?: string;
  name?: string;
  description?: string;
  category?: string;
  level?: string;
  duration?: number;
  durationHours?: number;
}

interface Enrollment {
  id?: string | number;
  enrollmentId?: string | number;
  courseId?: string | number;
  course?: Course;
  progress?: number;
  status?: string;
  completed?: boolean;
}

interface LearningPathItem {
  course: Course;
  progress: number;
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED';
  enrollmentId?: string | number;
}

@Component({
  selector: 'app-learning-path',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './learning-path.component.html',
  styleUrl: './learning-path.component.scss'
})
export class LearningPathComponent implements OnInit {

  private learningService = inject(LearningService);
  private authService = inject(KeycloakAuthService);
  private employeeService = inject(EmployeeService);
  private http = inject(HttpClient);

  courses: Course[] = [];
  enrollments: Enrollment[] = [];

  learningPath: LearningPathItem[] = [];

  loading = true;
  errorMessage = '';

  ngOnInit(): void {
    this.loadLearningPath();
  }

  private readonly employeeUrl =
  'http://localhost:8090/api/employees';

  get username(): string {
    return this.authService.getUsername() ?? 'Guest';
  }

  get completedCount(): number {
    return this.learningPath.filter(
      item => item.status === 'COMPLETED'
    ).length;
  }

  get inProgressCount(): number {
    return this.learningPath.filter(
      item => item.status === 'IN_PROGRESS'
    ).length;
  }


  
  get overallProgress(): number {
    if (!this.learningPath.length) {
      return 0;
    }

    const total = this.learningPath.reduce(
      (sum, item) => sum + item.progress,
      0
    );

    return Math.round(total / this.learningPath.length);
  }

  get nextCourse(): LearningPathItem | undefined {
    return this.learningPath.find(
      item => item.status !== 'COMPLETED'
    );
  }

// loadEnrollments(): void {

//   this.loading = true;

//   this.employeeService
//     .getCurrentEmployee()
//     .subscribe({

//       next: (employee) => {

//         const employeeId = employee?.employeeId;

//         if (!employeeId) {
//           this.loading = false;
//           this.errorMessage =
//             'Unable to identify the employee.';
//           return;
//         }

//         this.learningService
//           .getEnrollments(employeeId)
//           .subscribe({

//             next: (data) => {

//               this.enrollments =
//                 Array.isArray(data) ? data : [];

//               this.loading = false;
//             },

//             error: (error) => {

//               console.error(
//                 'Enrollment API failed:',
//                 error
//               );

//               this.loading = false;
//               this.errorMessage =
//                 'Unable to load your enrollments.';
//             }
//           });
//       },

//       error: (error) => {

//         console.error(
//           'Failed to load current employee:',
//           error
//         );

//         this.loading = false;
//         this.errorMessage =
//           'Unable to identify the logged-in employee.';
//       }
//     });
// }

private loadLearningPath(): void {
  this.loading = true;
  this.errorMessage = '';

  // First get the actual employee record
  this.employeeService.getCurrentEmployee().subscribe({

    next: (employee) => {

      console.log('Current employee:', employee);

      const employeeId = employee?.employeeId;

      if (!employeeId) {
        this.loading = false;
        this.errorMessage =
          'Unable to identify the logged-in employee.';
        return;
      }

      console.log(
        'Using employee UUID for enrollments:',
        employeeId
      );

      // Load courses
      this.learningService.getCourses().subscribe({

        next: (courses) => {

          this.courses =
            Array.isArray(courses)
              ? courses
              : [];

          // Load enrollments using EMPLOYEE UUID
          this.learningService
            .getEnrollments(employeeId)
            .subscribe({

              next: (enrollments) => {

                console.log(
                  'Employee enrollments:',
                  enrollments
                );

                this.enrollments =
                  Array.isArray(enrollments)
                    ? enrollments
                    : [];

                this.buildLearningPath();

                this.loading = false;
              },

              error: (error) => {

                console.error(
                  'Enrollment API failed:',
                  error
                );

                this.enrollments = [];

                this.buildLearningPath();

                this.loading = false;

                this.errorMessage =
                  'Unable to load your enrollments.';
              }

            });
        },

        error: (error) => {

          console.error(
            'Courses API failed:',
            error
          );

          this.errorMessage =
            'Unable to load learning path. Please try again.';

          this.loading = false;
        }

      });
    },

    error: (error) => {

      console.error(
        'Failed to load current employee:',
        error
      );

      this.loading = false;

      this.errorMessage =
        'Unable to identify the logged-in employee.';
    }

  });
}

  private buildLearningPath(): void {
    const enrollmentMap = new Map<string, Enrollment>();

    for (const enrollment of this.enrollments) {
      const courseId = this.getCourseIdFromEnrollment(enrollment);

      if (courseId !== null) {
        enrollmentMap.set(courseId, enrollment);
      }
    }

    this.learningPath = this.courses.map(course => {
      const courseId = this.getCourseId(course);
      const enrollment = courseId
        ? enrollmentMap.get(courseId)
        : undefined;

      const progress = this.getProgress(enrollment);

      return {
        course,
        progress,
        status: this.getStatus(enrollment, progress),
        enrollmentId:
          enrollment?.id ??
          enrollment?.enrollmentId
      };
    });
  }

  private getCourseId(course: Course): string | null {
    const id = course.id ?? course.courseId;

    return id !== undefined && id !== null
      ? String(id)
      : null;
  }

  private getCourseIdFromEnrollment(
    enrollment: Enrollment
  ): string | null {

    const id =
      enrollment.courseId ??
      enrollment.course?.id ??
      enrollment.course?.courseId;

    return id !== undefined && id !== null
      ? String(id)
      : null;
  }

  private getProgress(
    enrollment?: Enrollment
  ): number {

    if (!enrollment) {
      return 0;
    }

    if (enrollment.completed === true) {
      return 100;
    }

    const progress = Number(enrollment.progress ?? 0);

    return Math.min(100, Math.max(0, progress));
  }

  private getStatus(
    enrollment: Enrollment | undefined,
    progress: number
  ): 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED' {

    if (
      enrollment?.completed === true ||
      progress >= 100 ||
      enrollment?.status?.toUpperCase() === 'COMPLETED'
    ) {
      return 'COMPLETED';
    }

    if (
      progress > 0 ||
      enrollment?.status?.toUpperCase() === 'IN_PROGRESS'
    ) {
      return 'IN_PROGRESS';
    }

    return 'NOT_STARTED';
  }

enroll(item: LearningPathItem): void {

  const courseId = this.getCourseId(item.course);

  if (!courseId) {
    return;
  }

  this.employeeService
    .getCurrentEmployee()
    .subscribe({

      next: (employee) => {

        const employeeId = employee?.employeeId;

        if (!employeeId) {
          this.errorMessage =
            'Unable to identify the logged-in employee.';
          return;
        }

        console.log(
          'Enrolling employee:',
          employeeId,
          'course:',
          courseId
        );

        this.learningService
          .enroll(employeeId, courseId)
          .subscribe({

            next: (response) => {

              console.log(
                'Enrollment successful:',
                response
              );

              this.loadLearningPath();
            },

            error: (error) => {

              console.error(
                'Enrollment failed:',
                error
              );

              if (error.status === 409) {
                alert(
                  'You are already enrolled in this course.'
                );
              } else {
                alert(
                  'Unable to enroll in this course.'
                );
              }
            }

          });
      },

      error: (error) => {

        console.error(
          'Failed to identify employee:',
          error
        );

        this.errorMessage =
          'Unable to identify the logged-in employee.';
      }

    });
}

  getStatusLabel(status: string): string {
    switch (status) {
      case 'COMPLETED':
        return 'Completed';

      case 'IN_PROGRESS':
        return 'In Progress';

      default:
        return 'Not Started';
    }
  }
}