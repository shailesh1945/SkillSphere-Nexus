import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

import { LearningService } from '../learning.service';
import { KeycloakAuthService } from '../../services/keycloak-auth.service';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './course-details.component.html',
  styleUrl: './course-details.component.scss'
})
export class CourseDetailsComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private learningService = inject(LearningService);
  private authService = inject(KeycloakAuthService);
  private router = inject(Router);

  course: any = null;

  enrollment: any = null;

  loading = false;

  savingProgress = false;

  generatingCertificate = false;

  certificateGenerated = false;

  errorMessage = '';

  successMessage = '';

  ngOnInit(): void {
    this.loadCourse();
  }


  // ==========================================
  // LOAD COURSE
  // ==========================================

  loadCourse(): void {

    const courseId =
      this.route.snapshot.paramMap.get('courseId');

    if (!courseId) {

      this.errorMessage =
        'Course ID is missing.';

      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.learningService
      .getCourse(courseId)
      .subscribe({

        next: (data: any) => {

          console.log(
            'Course loaded:',
            data
          );

          this.course = data;

          this.loading = false;

          this.loadEnrollment(courseId);
        },

        error: (error) => {

          console.error(
            'Failed to load course:',
            error
          );

          this.loading = false;

          this.errorMessage =
            'Unable to load course details.';
        }
      });
  }


  // ==========================================
  // LOAD EMPLOYEE ENROLLMENT
  // ==========================================

  loadEnrollment(courseId: string): void {

    if (!this.isEmployee) {
      return;
    }

    const employeeId =
      this.authService.getUserId();

    if (!employeeId) {
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

          this.enrollment =
            enrollments.find(
              item =>
                String(item.courseId) ===
                String(courseId)
            ) ?? null;

          console.log(
            'Course enrollment:',
            this.enrollment
          );
        },

        error: (error) => {

          console.error(
            'Failed to load enrollment:',
            error
          );
        }
      });
  }


  // ==========================================
  // ROLES
  // ==========================================

  get isEmployee(): boolean {

    return this.authService.hasRole(
      'ROLE_EMPLOYEE'
    );
  }

  get isAdmin(): boolean {

    return this.authService.hasRole(
      'ROLE_ADMIN'
    );
  }

  get isTrainingManager(): boolean {

    return this.authService.hasRole(
      'ROLE_TRAINING_MANAGER'
    );
  }

  get canManageCourse(): boolean {

    return this.isAdmin ||
           this.isTrainingManager;
  }


  // ==========================================
  // PROGRESS
  // ==========================================

  get progress(): number {

    return Math.max(
      0,
      Math.min(
        100,
        Number(this.enrollment?.progress ?? 0)
      )
    );
  }


  get completed(): boolean {

    return this.enrollment?.completed === true ||
           this.progress >= 100;
  }


  onProgressChange(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    const value =
      Number(input.value);

    if (!this.enrollment) {
      return;
    }

    this.enrollment.progress =
      Math.max(
        0,
        Math.min(100, value)
      );

    this.errorMessage = '';
    this.successMessage = '';
  }


  // ==========================================
  // UPDATE PROGRESS
  // ==========================================

  updateCourseProgress(): void {

  if (!this.enrollment?.enrollmentId) {
    this.errorMessage =
      'Unable to identify your enrollment.';
    return;
  }

  this.savingProgress = true;
  this.errorMessage = '';
  this.successMessage = '';

  this.learningService
    .updateProgress(
      this.enrollment.enrollmentId,
      this.progress
    )
    .subscribe({

      next: (response) => {

        console.log(
          'Progress updated:',
          response
        );

        this.enrollment = {
          ...this.enrollment,
          ...response
        };

        if (this.progress >= 100) {

          this.completeCourse();

          return;
        }

        this.savingProgress = false;

        this.successMessage =
          `Course progress updated to ${this.progress}%.`;
      },

      error: (error) => {

        console.error(
          'Failed to update progress:',
          error
        );

        this.savingProgress = false;

        this.errorMessage =
          error?.error?.message ||
          'Unable to update course progress.';
      }
    });
}


  // ==========================================
  // COMPLETE COURSE
  // ==========================================

  completeCourse(): void {

  if (!this.enrollment?.enrollmentId) {

    this.savingProgress = false;

    this.errorMessage =
      'Unable to identify your enrollment.';

    return;
  }

  this.learningService
    .completeCourse(
      this.enrollment.enrollmentId
    )
    .subscribe({

      next: (response) => {

        console.log(
          'Course completed:',
          response
        );

        this.enrollment = {
          ...this.enrollment,
          ...response,
          progress: 100,
          completed: true
        };

        this.savingProgress = false;

        this.successMessage =
          'Course completed successfully. You can now generate your certificate.';
      },

      error: (error) => {

        console.error(
          'Failed to complete course:',
          error
        );

        this.savingProgress = false;

        this.errorMessage =
          error?.error?.message ||
          'Unable to complete the course.';
      }
    });
}


  // ==========================================
  // ENROLL
  // ==========================================

  enroll(): void {

    if (!this.course?.courseId) {
      return;
    }

    const employeeId =
      this.authService.getUserId();

    if (!employeeId) {

      this.errorMessage =
        'Unable to identify the logged-in employee.';

      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.learningService
      .enroll(
        employeeId,
        String(this.course.courseId)
      )
      .subscribe({

        next: (response) => {

          console.log(
            'Enrollment successful:',
            response
          );

          this.enrollment = response;

          this.loading = false;

          this.successMessage =
            'You have successfully enrolled in this course.';
        },

        error: (error) => {

          console.error(
            'Enrollment failed:',
            error
          );

          this.loading = false;

          if (error.status === 409) {

            this.errorMessage =
              'You are already enrolled in this course.';

          } else if (error.status === 403) {

            this.errorMessage =
              'You are not authorized to enroll in this course.';

          } else {

            this.errorMessage =
              'Unable to enroll in this course.';
          }
        }
      });
  }


  // ==========================================
  // CONTINUE LEARNING
  // ==========================================

  continueLearning(): void {

    if (!this.enrollment) {
      return;
    }

    console.log(
      'Continue learning:',
      this.enrollment
    );

    this.successMessage =
      'Course content is ready for the next learning step.';
  }


  // ==========================================
  // GENERATE CERTIFICATE
  // ==========================================

  generateCertificate(): void {

    if (!this.enrollment?.enrollmentId) {

      this.errorMessage =
        'Unable to identify your enrollment.';

      return;
    }

    if (!this.completed) {

      this.errorMessage =
        'Complete the course before generating a certificate.';

      return;
    }

    this.generatingCertificate = true;

    this.errorMessage = '';
    this.successMessage = '';

    this.learningService
      .generateCertificate(
        this.enrollment.enrollmentId
      )
      .subscribe({

        next: (response) => {

          console.log(
            'Certificate generated:',
            response
          );

          this.generatingCertificate = false;

          this.certificateGenerated = true;

          this.successMessage =
            'Certificate generated successfully.';
        },

        error: (error) => {

          console.error(
            'Certificate generation failed:',
            error
          );

          this.generatingCertificate = false;

          if (error.status === 409) {

            this.errorMessage =
              'A certificate has already been generated for this course.';

          } else if (error.status === 403) {

            this.errorMessage =
              'You are not authorized to generate this certificate.';

          } else {

            this.errorMessage =
              error?.error?.message ||
              'Failed to generate certificate.';
          }
        }
      });
  }


  // ==========================================
  // VIEW CERTIFICATIONS
  // ==========================================

  viewCertifications(): void {

    this.router.navigate([
      '/certifications'
    ]);
  }


  // ==========================================
  // BACK
  // ==========================================

  backToCourses(): void {

    this.router.navigate([
      '/learning/courses'
    ]);
  }
}