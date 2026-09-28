import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CertificationService } from '../certification.service';
import { KeycloakAuthService } from '../../services/keycloak-auth.service';

import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-certification-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './certification-list.component.html',
  styleUrl: './certification-list.component.scss',
})
export class CertificationListComponent implements OnInit {
  private certificationService = inject(CertificationService);
  private authService = inject(KeycloakAuthService);

  private employeeService = inject(EmployeeService);

  certifications: any[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  showRenewalRequests = false;

  renewalRequests: any[] = [];
  loadingRenewals = false;



    // =========================
  // ADD CERTIFICATION
  // =========================

  showAddForm = false;
  savingCertification = false;

  employees: any[] = [];

  newCertification = {
    certificationName: '',
    issuingOrganization: '',
    issueDate: '',
    expiryDate: '',
    credentialId: '',
    credentialUrl: '',
    employeeId: ''
  };

  ngOnInit(): void {
    this.loadCertifications();

    if (this.canManageCertifications) {
      this.loadRenewalRequests();
    }
  }

  get isEmployee(): boolean {
    return this.authService.hasRole('ROLE_EMPLOYEE');
  }

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get canManageCertifications(): boolean {
    return this.isHr || this.isAdmin;
  }

  loadCertifications(): void {
    this.loading = true;
    this.errorMessage = '';

    if (this.isEmployee) {
      this.employeeService.getCurrentEmployee().subscribe({
        next: (employee) => {
          console.log('Current employee:', employee);

          const employeeId = employee?.employeeId;

          if (!employeeId) {
            this.loading = false;

            this.errorMessage = 'Unable to identify your employee record.';

            return;
          }

          console.log('Loading certifications for employee:', employeeId);

          this.certificationService
            .getEmployeeCertifications(employeeId)
            .subscribe({
              next: (data: any[]) => {
                console.log('Employee certifications:', data);

                this.certifications = Array.isArray(data) ? data : [];

                this.loading = false;
              },

              error: (error) => {
                console.error('Error loading employee certifications:', error);

                this.certifications = [];
                this.loading = false;

                if (error.status === 403) {
                  this.errorMessage =
                    'You are not authorized to view these certifications.';
                } else {
                  this.errorMessage = 'Unable to load your certifications.';
                }
              },
            });
        },

        error: (error) => {
          console.error('Failed to load current employee:', error);

          this.loading = false;
          this.certifications = [];

          this.errorMessage = 'Unable to identify your employee record.';
        },
      });

      return;
    }

    if (this.isHr || this.isAdmin) {
      this.certificationService.getAllCertifications().subscribe({
        next: (data: any[]) => {
          this.certifications = Array.isArray(data) ? data : [];

          this.loading = false;
        },

        error: (error) => {
          this.certifications = [];
          this.loading = false;

          this.errorMessage = 'Unable to load certifications.';
        },
      });

      return;
    }

    this.loading = false;

    this.errorMessage = 'You do not have access to certifications.';
  }

  trackByCertificationId(index: number, cert: any): string {
    return cert.certificationId ?? cert.id ?? index.toString();
  }

  getValidCount(): number {
    return this.certifications.filter((cert) => cert.status === 'VALID').length;
  }

  getExpiredCount(): number {
    return this.certifications.filter((cert) => cert.status === 'EXPIRED')
      .length;
  }

  getExpiringCount(): number {
    return this.certifications.filter(
      (cert) =>
        cert.status === 'EXPIRING_SOON' || cert.status === 'PENDING_RENEWAL',
    ).length;
  }

  canRequestRenewal(cert: any): boolean {
    if (cert.status === 'PENDING_RENEWAL') {
      return false;
    }

    if (cert.status === 'EXPIRED') {
      return true;
    }

    if (cert.status === 'EXPIRING_SOON') {
      return true;
    }

    if (!cert.expiryDate) {
      return false;
    }

    const expiryDate = new Date(cert.expiryDate);

    const today = new Date();

    const thirtyDaysFromNow = new Date();

    thirtyDaysFromNow.setDate(today.getDate() + 30);

    return expiryDate >= today && expiryDate <= thirtyDaysFromNow;
  }

  toggleRenewalRequests(): void {
    this.showRenewalRequests = !this.showRenewalRequests;

    if (this.showRenewalRequests && this.canManageCertifications) {
      this.loadRenewalRequests();
    }
  }

  loadRenewalRequests(): void {
    this.loadingRenewals = true;

    this.certificationService.getRenewalRequests().subscribe({
      next: (data: any[]) => {
        this.renewalRequests = Array.isArray(data) ? data : [];

        this.loadingRenewals = false;

        console.log('Renewal requests:', this.renewalRequests);
      },

      error: (error) => {
        console.error('Failed to load renewal requests:', error);

        this.loadingRenewals = false;
        this.renewalRequests = [];
      },
    });
  }

  requestRenewal(cert: any): void {
    const employeeId = this.authService.getUserId();

    const certificationId = cert.certificationId ?? cert.id;

    if (!employeeId) {
      this.errorMessage = 'Unable to identify the logged-in employee.';

      return;
    }

    if (!certificationId) {
      this.errorMessage = 'Certification ID is missing.';

      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.certificationService
      .requestRenewal(certificationId, employeeId)
      .subscribe({
        next: (response) => {
          console.log('Renewal requested:', response);

          this.loading = false;

          this.successMessage = 'Renewal request submitted successfully.';

          this.loadCertifications();
        },

        error: (error) => {
          console.error('Renewal request failed:', error);

          this.loading = false;

          if (error.status === 409) {
            this.errorMessage =
              'A renewal request already exists for this certificate.';
          } else if (error.status === 403) {
            this.errorMessage =
              'You are not authorized to request this renewal.';
          } else {
            this.errorMessage = 'Unable to submit the renewal request.';
          }
        },
      });
  }

  selectedCertification: any = null;

  showCertificationDetails = false;

  loadingDetails = false;

  detailsError = '';

  viewCertification(certification: any): void {
    const certificationId = certification.certificationId ?? certification.id;

    if (!certificationId) {
      console.error('Certification ID not found:', certification);

      this.detailsError = 'Unable to identify this certification.';

      return;
    }

    this.loadingDetails = true;

    this.showCertificationDetails = true;

    this.selectedCertification = null;

    this.detailsError = '';

    this.certificationService.getById(String(certificationId)).subscribe({
      next: (data) => {
        console.log('Certification details:', data);

        this.selectedCertification = data;

        this.loadingDetails = false;
      },

      error: (error) => {
        console.error('Failed to load certification:', error);

        this.loadingDetails = false;

        this.detailsError =
          error?.error?.message || 'Unable to load certification details.';
      },
    });
  }

  closeCertificationDetails(): void {
    this.showCertificationDetails = false;

    this.selectedCertification = null;

    this.detailsError = '';
  }

  viewReport(cert: any): void {
    console.log('Certification report:', cert);

    /*
     * HR/Admin report functionality will be connected
     * to the actual report endpoint.
     */
  }

  showApprovalForm = false;

  selectedRenewal: any = null;

  newExpiry = '';

  openApproval(renewal: any): void {
    this.selectedRenewal = renewal;

    this.newExpiry = '';

    this.showApprovalForm = true;

    this.successMessage = '';
    this.errorMessage = '';
  }

 approveRenewal(): void {

  if (!this.selectedRenewal) {
    return;
  }

  if (!this.newExpiry) {
    this.errorMessage = 'Please select a new expiry date.';
    return;
  }

  const approvedBy = this.authService.getUsername();

  if (!approvedBy) {
    this.errorMessage = 'Unable to identify the HR user.';
    return;
  }

  console.log('Approving renewal:', {
    renewalId: this.selectedRenewal.renewalId,
    newExpiry: this.newExpiry,
    approvedBy: approvedBy
  });

  this.certificationService
    .approveRenewal(
      this.selectedRenewal.renewalId,
      this.newExpiry,
      approvedBy
    )
    .subscribe({

      next: (response) => {

        console.log('Renewal approved successfully:', response);

        this.successMessage =
          'Certification renewal approved successfully.';

        this.errorMessage = '';

        this.cancelApproval();

        this.loadCertifications();
        this.loadRenewalRequests();
      },

      error: (error) => {

        console.error(
          'Failed to approve renewal:',
          error
        );

        console.error(
          'Status:',
          error.status
        );

        console.error(
          'Backend response:',
          error.error
        );

        this.errorMessage =
          error?.error?.message ||
          `Failed to approve renewal. HTTP ${error.status}`;
      }

    });
}

  cancelApproval(): void {
    this.showApprovalForm = false;

    this.selectedRenewal = null;

    this.newExpiry = '';
  }


    openAddCertificationForm(): void {
    this.resetCertificationForm();

    this.showAddForm = true;

    this.errorMessage = '';
    this.successMessage = '';

    this.loadEmployeesForCertification();
  }

  closeAddCertificationForm(): void {
    if (this.savingCertification) {
      return;
    }

    this.showAddForm = false;
  }

  loadEmployeesForCertification(): void {
    this.employeeService.getEmployees().subscribe({
      next: (employees) => {
        this.employees = Array.isArray(employees)
          ? employees
          : [];
      },

      error: (error) => {
        console.error(
          'Failed to load employees:',
          error
        );

        this.errorMessage =
          'Unable to load employees.';
      }
    });
  }

  addCertification(): void {

    if (
      !this.newCertification.certificationName.trim() ||
      !this.newCertification.employeeId
    ) {
      this.errorMessage =
        'Please enter certification name and select an employee.';
      return;
    }

    if (
      this.newCertification.issueDate &&
      this.newCertification.expiryDate &&
      this.newCertification.expiryDate <
        this.newCertification.issueDate
    ) {
      this.errorMessage =
        'Expiry date cannot be before the issue date.';
      return;
    }

    this.savingCertification = true;

    this.errorMessage = '';
    this.successMessage = '';

    const payload = {
      certificationName:
        this.newCertification.certificationName.trim(),

      issuingOrganization:
        this.newCertification.issuingOrganization.trim() || null,

      issueDate:
        this.newCertification.issueDate || null,

      expiryDate:
        this.newCertification.expiryDate || null,

      credentialId:
        this.newCertification.credentialId.trim() || null,

      credentialUrl:
        this.newCertification.credentialUrl.trim() || null,

      employeeId:
        this.newCertification.employeeId
    };

    console.log(
      'Creating certification:',
      payload
    );

    this.certificationService
      .register(payload)
      .subscribe({

        next: (response) => {

          console.log(
            'Certification created:',
            response
          );

          this.savingCertification = false;

          this.showAddForm = false;

          this.successMessage =
            'Certification added successfully.';

          this.resetCertificationForm();

          this.loadCertifications();
        },

        error: (error) => {

          console.error(
            'Certification creation failed:',
            error
          );

          this.savingCertification = false;

          this.errorMessage =
            error?.error?.message ||
            'Unable to add certification.';
        }
      });
  }

  resetCertificationForm(): void {

    this.newCertification = {
      certificationName: '',
      issuingOrganization: '',
      issueDate: '',
      expiryDate: '',
      credentialId: '',
      credentialUrl: '',
      employeeId: ''
    };
  }

}
