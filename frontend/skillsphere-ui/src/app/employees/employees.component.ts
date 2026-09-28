import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { EmployeeService } from '../services/employee.service';
import { KeycloakAuthService } from '../services/keycloak-auth.service';

@Component({
  selector: 'app-employees',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './employees.component.html',
  styleUrl: './employees.component.scss'
})
export class EmployeesComponent implements OnInit {

  private employeeService = inject(EmployeeService);
  private router = inject(Router);
  private authService = inject(KeycloakAuthService);

  employees: any[] = [];

  filteredEmployees: any[] = [];

  searchText = '';

  loading = true;

  errorMessage = '';

  successMessage = '';

  // Add employee form
  showAddEmployee = false;

  saving = false;

  newEmployee = {
  firstName: '',
  lastName: '',
  email: '',
  username: '',
  temporaryPassword: '',
  phoneNumber: '',
  department: '',
  joiningDate: '',
  role: 'DEVELOPER'
};

  ngOnInit(): void {
    this.loadEmployees();
  }

  // =========================
  // ROLE CHECK
  // =========================

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get canManageEmployees(): boolean {
    return this.isHr || this.isAdmin;
  }

  // =========================
  // LOAD EMPLOYEES
  // =========================

  loadEmployees(): void {

    this.loading = true;
    this.errorMessage = '';

    this.employeeService.getEmployees().subscribe({

      next: (response: any) => {

        if (Array.isArray(response)) {
          this.employees = response;

        } else if (response?.content) {
          this.employees = response.content;

        } else if (response?.data) {
          this.employees = response.data;

        } else {
          this.employees = [];
        }

        this.filteredEmployees =
          [...this.employees];

        this.loading = false;
      },

      error: (error) => {

        console.error(
          'Employees API failed:',
          error
        );

        this.errorMessage =
          'Unable to load employees. Please try again.';

        this.loading = false;
      }
    });
  }

  // =========================
  // ADD EMPLOYEE
  // =========================

  openAddEmployee(): void {

    if (!this.canManageEmployees) {
      return;
    }

    this.resetEmployeeForm();

    this.showAddEmployee = true;

    this.successMessage = '';
    this.errorMessage = '';
  }

  cancelAddEmployee(): void {

    this.showAddEmployee = false;

    this.resetEmployeeForm();
  }

  addEmployee(): void {

  if (!this.canManageEmployees) {
    return;
  }

  // Frontend validation
  if (
    !this.newEmployee.firstName.trim() ||
    !this.newEmployee.lastName.trim() ||
    !this.newEmployee.email.trim() ||
    !this.newEmployee.username.trim() ||
    !this.newEmployee.temporaryPassword.trim() ||
    !this.newEmployee.role.trim()
  ) {

    this.errorMessage =
      'First name, last name, email, username, temporary password and role are required.';

    return;
  }

  this.saving = true;
  this.errorMessage = '';
  this.successMessage = '';

  const employee = {

    firstName:
      this.newEmployee.firstName.trim(),

    lastName:
      this.newEmployee.lastName.trim(),

    email:
      this.newEmployee.email.trim(),

    username:
      this.newEmployee.username.trim(),

    temporaryPassword:
      this.newEmployee.temporaryPassword,

    phoneNumber:
      this.newEmployee.phoneNumber.trim(),

    department:
      this.newEmployee.department.trim(),

    joiningDate:
      this.newEmployee.joiningDate || null,

    role:
      this.newEmployee.role.trim().toUpperCase()
  };

  console.log(
    'Creating employee:',
    {
      ...employee,
      temporaryPassword: '********'
    }
  );

  this.employeeService
    .addEmployee(employee)
    .subscribe({

      next: (response) => {

        console.log(
          'Employee created successfully:',
          response
        );

        this.successMessage =
          'Employee created successfully. The employee can now log in using the new Keycloak account.';

        this.saving = false;

        this.showAddEmployee = false;

        this.resetEmployeeForm();

        // Reload employee list
        this.loadEmployees();
      },

      error: (error) => {

        console.error(
          'Add employee failed:',
          error
        );

        this.saving = false;

        if (error.status === 400) {

          this.errorMessage =
            error?.error?.message ||
            'Invalid employee details. Please check the form.';

        } else if (error.status === 403) {

          this.errorMessage =
            'You are not authorized to add employees.';

        } else if (error.status === 409) {

          this.errorMessage =
            'Username or email already exists. Please use different values.';

        } else {

          this.errorMessage =
            error?.error?.message ||
            'Unable to create employee. Please try again.';
        }
      }
    });
}

resetEmployeeForm(): void {

  this.newEmployee = {
    firstName: '',
    lastName: '',
    email: '',
    username: '',
    temporaryPassword: '',
    phoneNumber: '',
    department: '',
    joiningDate: '',
    role: 'DEVELOPER'
  };
}

  // =========================
  // SEARCH
  // =========================

  filterEmployees(): void {

    const search =
      this.searchText
        .trim()
        .toLowerCase();

    if (!search) {

      this.filteredEmployees =
        [...this.employees];

      return;
    }

    this.filteredEmployees =
      this.employees.filter(employee => {

        const text = [
          employee.id,
          employee.employeeId,
          employee.empId,
          employee.name,
          employee.firstName,
          employee.lastName,
          employee.email,
          employee.department,
          employee.role,
          employee.currentRole
        ]
          .filter(
            value =>
              value !== undefined &&
              value !== null
          )
          .join(' ')
          .toLowerCase();

        return text.includes(search);
      });
  }

  // =========================
  // DISPLAY HELPERS
  // =========================

  getEmployeeId(employee: any): string {

    return String(
      employee.employeeId ??
      employee.empId ??
      employee.id ??
      ''
    );
  }

  getEmployeeName(employee: any): string {

    if (employee.name) {
      return employee.name;
    }

    const fullName = [
      employee.firstName,
      employee.lastName
    ]
      .filter(Boolean)
      .join(' ');

    return fullName || 'Unknown Employee';
  }

  getEmployeeRole(employee: any): string {

    return (
      employee.role ??
      employee.currentRole ??
      employee.designation ??
      '—'
    );
  }

  getDepartment(employee: any): string {

    return (
      employee.department ??
      employee.departmentName ??
      '—'
    );
  }

  // =========================
  // VIEW EMPLOYEE
  // =========================

  viewEmployee(employee: any): void {

    const employeeId =
      this.getEmployeeId(employee);

    if (!employeeId) {
      return;
    }

    this.router.navigate([
      '/employees',
      employeeId
    ]);
  }
}