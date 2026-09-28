import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { LearningService } from '../learning.service';
import { KeycloakAuthService } from '../../services/keycloak-auth.service';

@Component({
  selector: 'app-enrollment',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './enrollment.component.html',
  styleUrl: './enrollment.component.scss'
})
export class EnrollmentComponent implements OnInit {

  private learningService = inject(LearningService);
  private authService = inject(KeycloakAuthService);
  private router = inject(Router);

  enrollments: any[] = [];

  loading = false;

  errorMessage = '';

  ngOnInit(): void {
    this.loadMyLearning();
  }

  loadMyLearning(): void {

    const employeeId =
      this.authService.getUserId();

    if (!employeeId) {

      this.errorMessage =
        'Unable to identify the logged-in employee.';

      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.learningService
      .getEnrollments(employeeId)
      .subscribe({

        next: (data: any[]) => {

          this.enrollments =
            Array.isArray(data)
              ? data
              : [];

          this.loading = false;

        },

        error: (error) => {

          console.error(
            'Failed to load my learning:',
            error
          );

          this.loading = false;

          if (error.status === 403) {
            this.errorMessage =
              'You are not authorized to view your learning.';
          } else {
            this.errorMessage =
              'Unable to load your learning courses.';
          }
        }
      });
  }

  get completedCount(): number {

    return this.enrollments.filter(
      enrollment =>
        enrollment.completed === true ||
        Number(enrollment.progress) >= 100
    ).length;
  }

  get inProgressCount(): number {

    return this.enrollments.filter(
      enrollment =>
        !enrollment.completed &&
        Number(enrollment.progress ?? 0) < 100
    ).length;
  }

  get averageProgress(): number {

    if (this.enrollments.length === 0) {
      return 0;
    }

    const total =
      this.enrollments.reduce(
        (sum, enrollment) =>
          sum + Number(enrollment.progress ?? 0),
        0
      );

    return Math.round(
      total / this.enrollments.length
    );
  }

  continueLearning(enrollment: any): void {

    this.router.navigate([
      '/learning/courses',
      enrollment.courseId
    ]);
  }

  generateCertificate(enrollment: any): void {

    if (!enrollment.completed) {

      return;
    }

    this.learningService
      .generateCertificate(
        enrollment.enrollmentId
      )
      .subscribe({

        next: (response) => {

          console.log(
            'Certificate generated:',
            response
          );

          alert(
            'Certificate generated successfully!'
          );

        },

        error: (error) => {

          console.error(
            'Certificate generation failed:',
            error
          );

          if (error.status === 403) {

            alert(
              'You do not have permission to generate this certificate.'
            );

          } else {

            alert(
              'Failed to generate certificate.'
            );
          }
        }
      });
  }

  browseCourses(): void {

    this.router.navigate([
      '/learning/courses'
    ]);
  }
}