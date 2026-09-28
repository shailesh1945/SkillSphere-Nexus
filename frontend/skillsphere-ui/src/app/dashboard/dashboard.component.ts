import { Component, OnInit, inject } from '@angular/core';
import { DashboardService } from '../services/dashboard.service';
import { KeycloakAuthService } from '../services/keycloak-auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  private dashboardService = inject(DashboardService);

  loading = true;
  error = false;

  dashboard = {
    employees: 0,
    skills: 0,
    courses: 0,
    expiringCertifications: 0,
    careerPlans: 0,
    analytics: null as any
  };

  ngOnInit(): void {
    this.loadDashboard();
  }

  loadDashboard(): void {

    this.loading = true;
    this.error = false;

    this.dashboardService.getDashboardData().subscribe({

      next: (data) => {

        this.dashboard = {
          employees: data.employees ?? 0,
          skills: data.skills ?? 0,
          courses: data.courses ?? 0,
          expiringCertifications:
            data.expiringCertifications ?? 0,
          careerPlans:
            data.careerPlans ?? 0,
          analytics:
            data.analytics ?? null
        };

        this.loading = false;
      },

      error: (error) => {

        console.error(
          'Dashboard loading failed:',
          error
        );

        this.error = true;
        this.loading = false;
      }

    });
  }

  authService = inject(KeycloakAuthService);
  
    get username(): string {
      return this.authService.getUsername() ?? 'Guest';
    }

    get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get isTrainingManager(): boolean {
    return this.authService.hasRole('ROLE_TRAINING_MANAGER');
  }

  get activeJobs(): number {
    return this.dashboard.analytics?.activeJobs ?? 0;
  }

  get promotions(): number {
    return this.dashboard.analytics?.promotions ?? 0;
  }

  get averageCompletionRate(): number {
    return this.dashboard.analytics?.completionRate ?? 0;
  }
}