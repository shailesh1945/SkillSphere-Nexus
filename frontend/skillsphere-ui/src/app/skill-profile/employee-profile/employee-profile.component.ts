import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-employee-profile',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './employee-profile.component.html',
  styleUrl: './employee-profile.component.scss'
})
export class EmployeeProfileComponent implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private employeeService = inject(EmployeeService);

  employee: any = null;

  loading = true;

  errorMessage = '';

  ngOnInit(): void {

    const empId =
      this.route.snapshot.paramMap.get('empId');

    if (!empId) {

      this.errorMessage =
        'Employee ID was not provided.';

      this.loading = false;

      return;
    }

    this.loadEmployee(empId);
  }

  loadEmployee(empId: string): void {

    this.employeeService
      .getEmployee(empId)
      .subscribe({

        next: (employee) => {

          this.employee = employee;

          this.loading = false;
        },

        error: (error) => {

          console.error(
            'Employee profile API failed:',
            error
          );

          this.errorMessage =
            'Unable to load employee profile.';

          this.loading = false;
        }
      });
  }

  getEmployeeName(): string {

    if (!this.employee) {
      return '';
    }

    if (this.employee.name) {
      return this.employee.name;
    }

    return [
      this.employee.firstName,
      this.employee.lastName
    ]
      .filter(Boolean)
      .join(' ') || 'Unknown Employee';
  }

  getRole(): string {

    return (
      this.employee?.role ??
      this.employee?.currentRole ??
      this.employee?.designation ??
      '—'
    );
  }

  getDepartment(): string {

    return (
      this.employee?.department ??
      this.employee?.departmentName ??
      '—'
    );
  }

  goBack(): void {
    this.router.navigate(['/employees']);
  }


  
}