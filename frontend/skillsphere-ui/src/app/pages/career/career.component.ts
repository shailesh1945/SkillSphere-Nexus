import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CareerService } from '../../services/career.service';
import { KeycloakAuthService } from '../../services/keycloak-auth.service';

@Component({
  selector: 'app-career',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './career.component.html',
  styleUrl: './career.component.scss'
})
export class CareerComponent implements OnInit {

  private careerService = inject(CareerService);
  private authService = inject(KeycloakAuthService);

  plans: any[] = [];

  employees: any[] = [];

  loading = false;
  loadingEmployees = false;
  saving = false;

  errorMessage = '';
  successMessage = '';

  showAddForm = false;

  newPlan: any = {
    employeeId: '',
    employeeName: '',
    currentRole: '',
    targetRole: '',
    progress: 0,
    mentor: '',
    skillGaps: '',
    trainingPlan: ''
  };


  // ============================
  // ROLES
  // ============================

  get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get isEmployee(): boolean {
    return this.authService.hasRole('ROLE_EMPLOYEE');
  }

  get canManageCareerPlans(): boolean {
    return this.isAdmin || this.isHr;
  }


  // ============================
  // INIT
  // ============================

  ngOnInit(): void {
    this.loadPlans();

    if (this.canManageCareerPlans) {
      this.loadEmployees();
    }
  }


  // ============================
  // LOAD CAREER PLANS
  // ============================

  loadPlans(): void {

    this.loading = true;
    this.errorMessage = '';

    this.careerService
      .getCareerPlans()
      .subscribe({

        next: (data) => {

          this.plans =
            Array.isArray(data)
              ? data
              : [];

          this.loading = false;
        },

        error: (error) => {

          console.error(
            'Error loading career plans:',
            error
          );

          this.loading = false;
          this.plans = [];

          this.errorMessage =
            'Unable to load career plans.';
        }
      });
  }


  // ============================
  // LOAD EMPLOYEES
  // ============================

  loadEmployees(): void {

    this.loadingEmployees = true;

    this.careerService
      .getEmployees()
      .subscribe({

        next: (data: any[]) => {

          console.log(
            'Employees loaded for career plan:',
            data
          );

          this.employees =
            Array.isArray(data)
              ? data
              : [];

          this.loadingEmployees = false;
        },

        error: (error) => {

          console.error(
            'Failed to load employees:',
            error
          );

          this.employees = [];

          this.loadingEmployees = false;

          this.errorMessage =
            'Unable to load employees.';
        }
      });
  }


  // ============================
  // EMPLOYEE SELECTED
  // ============================

  onEmployeeSelected(): void {

    const selectedEmployee =
      this.employees.find(
        employee =>
          String(employee.employeeId) ===
          String(this.newPlan.employeeId)
      );

    if (!selectedEmployee) {
      return;
    }

    this.newPlan.employeeName =
      `${selectedEmployee.firstName ?? ''} ${selectedEmployee.lastName ?? ''}`
        .trim();

  }


  // ============================
  // OPEN FORM
  // ============================

  openAddCareerPlan(): void {

    this.showAddForm = true;

    this.errorMessage = '';
    this.successMessage = '';

    this.resetForm();

    if (this.employees.length === 0) {
      this.loadEmployees();
    }
  }


  // ============================
  // CANCEL
  // ============================

  cancelAddCareerPlan(): void {

    this.showAddForm = false;

    this.resetForm();
  }


  // ============================
  // CREATE
  // ============================

  addCareerPlan(): void {

    if (!this.newPlan.employeeId) {

      this.errorMessage =
        'Please select an employee.';

      return;
    }

    if (!this.newPlan.currentRole) {

      this.errorMessage =
        'Please enter the current role.';

      return;
    }

    if (!this.newPlan.targetRole) {

      this.errorMessage =
        'Please enter the target role.';

      return;
    }


    if (
      this.newPlan.progress < 0 ||
      this.newPlan.progress > 100
    ) {

      this.errorMessage =
        'Progress must be between 0 and 100.';

      return;
    }


    this.saving = true;

    this.errorMessage = '';
    this.successMessage = '';


    this.careerService
      .createCareerPlan(this.newPlan)
      .subscribe({

        next: (response) => {

          console.log(
            'Career plan created:',
            response
          );

          this.saving = false;

          this.successMessage =
            'Career plan created successfully.';

          this.showAddForm = false;

          this.resetForm();

          this.loadPlans();
        },

        error: (error) => {

          console.error(
            'Failed to create career plan:',
            error
          );

          this.saving = false;

          this.errorMessage =
            error?.error?.message ||
            'Failed to create career plan.';
        }
      });
  }


  // ============================
  // DELETE
  // ============================

  deleteCareerPlan(plan: any): void {

    if (!plan?.planId) {
      return;
    }

    const confirmed =
      window.confirm(
        `Delete career plan for ${plan.employeeName}?`
      );

    if (!confirmed) {
      return;
    }


    this.careerService
      .deleteCareerPlan(plan.planId)
      .subscribe({

        next: () => {

          this.successMessage =
            'Career plan deleted successfully.';

          this.loadPlans();
        },

        error: (error) => {

          console.error(
            'Failed to delete career plan:',
            error
          );

          this.errorMessage =
            'Failed to delete career plan.';
        }
      });
  }


  // ============================
  // RESET
  // ============================

  resetForm(): void {

    this.newPlan = {

      employeeId: '',

      employeeName: '',

      currentRole: '',

      targetRole: '',

      progress: 0,

      mentor: '',

      skillGaps: '',

      trainingPlan: ''
    };
  }


  trackByPlanId(
    index: number,
    plan: any
  ): string {

    return plan.planId ?? index.toString();
  }
}