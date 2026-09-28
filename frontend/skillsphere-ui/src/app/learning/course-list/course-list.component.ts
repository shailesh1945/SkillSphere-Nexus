import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { LearningService } from '../learning.service';
import { KeycloakAuthService } from '../../services/keycloak-auth.service';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './course-list.component.html',
  styleUrl: './course-list.component.scss'
})
export class CourseListComponent implements OnInit {

  constructor(
    private router: Router
    ) {}

  private learningService = inject(LearningService);
  private authService = inject(KeycloakAuthService);

  courses: any[] = [];

  totalCourses = 0;
  enrollments = 0;
  completion = 0;
  completionRate = 0;

  showAddCourse = false;

  saving = false;

  successMessage = '';
  errorMessage = '';

  newCourse = {
    title: '',
    description: '',
    duration: 0
  };

  ngOnInit(): void {
    this.loadCourses();
  }

  get isEmployee(): boolean {
    return this.authService.hasRole('ROLE_EMPLOYEE');
  }
  get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get isTrainingManager(): boolean {
    return this.authService.hasRole('ROLE_TRAINING_MANAGER');
  }

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get canManageCourses(): boolean {
    return this.isAdmin || this.isTrainingManager;
  }

  loadCourses(): void {

    this.learningService.getCourses().subscribe({

      next: (data: any[]) => {

        this.courses = data ?? [];

        this.totalCourses = this.courses.length;

        this.loadStatistics();
      },

      error: (error) => {

        console.error(
          'Error loading courses:',
          error
        );

        this.errorMessage =
          'Unable to load courses.';
      }
    });
  }


  loadStatistics(): void {

  if (this.courses.length === 0) {
    this.enrollments = 0;
    this.completion = 0;
    this.completionRate = 0;
    return;
  }

  // ==========================================
  // EMPLOYEE
  // ==========================================

  if (this.isEmployee) {

    const employeeId =
      this.authService.getUserId();

    if (!employeeId) {
      console.error(
        'Employee UUID not available.'
      );
      return;
    }

    this.learningService
      .getEnrollments(employeeId)
      .subscribe({

        next: (data: any[]) => {

          const enrollments =
            Array.isArray(data)
              ? data
              : [];

          this.enrollments =
            enrollments.length;

          this.completion =
            enrollments.filter(
              enrollment =>
                Number(enrollment.progress) >= 100
            ).length;

          this.completionRate =
            this.enrollments > 0
              ? Math.round(
                  (
                    this.completion /
                    this.enrollments
                  ) * 100
                )
              : 0;

          this.courses =
            this.courses.map(course => {

              const enrollment =
                enrollments.find(
                  e =>
                    String(e.courseId) ===
                    String(course.courseId)
                );

              if (!enrollment) {

                return {
                  ...course,
                  enrolledByEmployee: false,
                  enrollmentId: null,
                  progress: 0,
                  score: null,
                  enrolled: 0,
                  completed: 0
                };
              }

              return {
                ...course,
                enrolledByEmployee: true,
                enrollmentId:
                  enrollment.enrollmentId,
                progress:
                  enrollment.progress ?? 0,
                score:
                  enrollment.score ?? null,
                completed:
                  enrollment.completed ?? false
              };
            });
        },

        error: (error) => {

          console.error(
            'Error loading employee enrollments:',
            error
          );

          this.enrollments = 0;
          this.completion = 0;
          this.completionRate = 0;
        }
      });

    return;
  }


  // ==========================================
  // HR / ADMIN / TRAINING MANAGER
  // ==========================================

  const requests =
    this.courses.map(course =>
      this.learningService
        .getCourseEnrollments(
          String(course.courseId)
        )
    );

  forkJoin(requests).subscribe({

    next: (results: any[][]) => {

      let totalEnrollments = 0;
      let totalCompleted = 0;

      this.courses =
        this.courses.map(
          (course, index) => {

            const courseEnrollments =
              Array.isArray(results[index])
                ? results[index]
                : [];

            const enrolled =
              courseEnrollments.length;

            const completed =
              courseEnrollments.filter(
                enrollment =>
                  enrollment.completed === true ||
                  Number(
                    enrollment.progress ?? 0
                  ) >= 100
              ).length;

            totalEnrollments += enrolled;
            totalCompleted += completed;

            return {
              ...course,

              enrolled,

              completed,

              enrollmentCount:
                enrolled,

              completedCount:
                completed
            };
          }
        );

      this.enrollments =
        totalEnrollments;

      this.completion =
        totalCompleted;

      this.completionRate =
        totalEnrollments > 0
          ? Math.round(
              (
                totalCompleted /
                totalEnrollments
              ) * 100
            )
          : 0;

      console.log(
        'Course statistics:',
        this.courses
      );
    },

    error: (error) => {

      console.error(
        'Failed to load course statistics:',
        error
      );

      this.enrollments = 0;
      this.completion = 0;
      this.completionRate = 0;
    }
  });
}


  openAddCourse(): void {

    this.newCourse = {
      title: '',
      description: '',
      duration: 0
    };

    this.successMessage = '';
    this.errorMessage = '';

    this.showAddCourse = true;
  }


  cancelAddCourse(): void {

    this.showAddCourse = false;

    this.successMessage = '';
    this.errorMessage = '';
  }


  addCourse(): void {

    if (
      !this.newCourse.title.trim() ||
      !this.newCourse.description.trim() ||
      this.newCourse.duration <= 0
    ) {

      this.errorMessage =
        'Please fill in all course details.';

      return;
    }


    this.saving = true;

    this.successMessage = '';
    this.errorMessage = '';


    this.learningService
      .createCourse(this.newCourse)
      .subscribe({

        next: () => {

          this.saving = false;

          this.showAddCourse = false;

          this.successMessage =
            'Course added successfully.';

          this.loadCourses();
        },

        error: (error) => {

          console.error(
            'Create course failed:',
            error
          );

          this.saving = false;

          this.errorMessage =
            'Unable to create course.';
        }
      });
  }

  generateCertificate(course: any): void {
  if (!course.enrollmentId) {
    alert('You are not enrolled in this course.');
    return;
  }

  if ((course.progress ?? 0) < 100) {
    alert('Complete the course before generating the certificate.');
    return;
  }

  this.learningService
    .generateCertificate(course.enrollmentId)
    .subscribe({
      next: (response) => {
        console.log('Certificate generated:', response);
        alert('Certificate generated successfully!');
      },
      error: (error) => {
        console.error('Certificate generation failed:', error);

        if (error.status === 403) {
          alert('You do not have permission to generate this certificate.');
        } else if (error.status === 404) {
          alert('Enrollment was not found.');
        } else {
          alert('Failed to generate certificate.');
        }
      }
    });
}

continueCourse(course: any): void {
  this.router.navigate([
    '/learning/courses',
    course.courseId
  ]);
}

  enroll(course: any): void {

  console.log('Enroll clicked:', course);

  // const employeeId = this.authService.getUsername();
  const employeeId =
  this.authService.getUserId();

  console.log('Employee ID:', employeeId);
  console.log('Course ID:', course.courseId);

  if (!employeeId) {
    this.errorMessage = 'Unable to identify the logged-in employee.';
    return;
  }

  if (!course.courseId) {
    this.errorMessage = 'Course ID is missing.';
    return;
  }

  this.successMessage = '';
  this.errorMessage = '';

  this.learningService
    .enroll(employeeId, String(course.courseId))
    .subscribe({

      next: (response) => {

        console.log(
          'Enrollment successful:',
          response
        );

        this.successMessage =
          `Successfully enrolled in "${course.title}".`;

        this.errorMessage = '';

        // Reload courses and enrollment information
        this.loadCourses();
      },

      error: (error) => {

        console.error(
          'Enrollment failed:',
          error
        );

        console.error(
          'Status:',
          error.status
        );

        console.error(
          'Response:',
          error.error
        );

        if (error.status === 403) {

          this.errorMessage =
            'You are not authorized to enroll in this course.';

        } else if (error.status === 404) {

          this.errorMessage =
            'Employee or course was not found.';

        } else if (error.status === 409) {

          this.errorMessage =
            'You are already enrolled in this course.';

        } else {

          this.errorMessage =
            'Enrollment failed. Check the browser console.';

        }

        this.successMessage = '';
      }
    });
}
}