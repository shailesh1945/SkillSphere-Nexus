import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CareerService } from '../../services/career.service';
import { KeycloakAuthService } from '../../services/keycloak-auth.service';

@Component({
  selector: 'app-jobs',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './jobs.component.html',
  styleUrl: './jobs.component.scss'
})
export class JobsComponent implements OnInit {

  private careerService = inject(CareerService);
  private authService = inject(KeycloakAuthService);

  jobs: any[] = [];

  showAddJob = false;

  loading = true;

  saving = false;

  errorMessage = '';

  successMessage = '';

  newJob = {
    title: '',
    department: '',
    requiredSkills: '',
    minimumExperience: 0
  };

  ngOnInit(): void {
    this.loadJobs();
  }

  get isHr(): boolean {
    return this.authService.hasRole('ROLE_HR');
  }

  get isAdmin(): boolean {
    return this.authService.hasRole('ROLE_ADMIN');
  }

  get canManageJobs(): boolean {
    return this.isHr || this.isAdmin;
  }

  loadJobs(): void {

    this.loading = true;

    this.careerService.getJobs().subscribe({

      next: (jobs) => {

        this.jobs = Array.isArray(jobs)
          ? jobs
          : [];

        this.loading = false;
      },

      error: (error) => {

        console.error(
          'Jobs API failed:',
          error
        );

        this.errorMessage =
          'Unable to load jobs.';

        this.loading = false;
      }
    });
  }

  openAddJob(): void {

    this.successMessage = '';
    this.errorMessage = '';

    this.newJob = {
      title: '',
      department: '',
      requiredSkills: '',
      minimumExperience: 0
    };

    this.showAddJob = true;
  }

  cancelAddJob(): void {
    this.showAddJob = false;
  }

  addJob(): void {

    if (
      !this.newJob.title.trim() ||
      !this.newJob.department.trim() ||
      !this.newJob.requiredSkills.trim()
    ) {

      this.errorMessage =
        'Please fill in all required fields.';

      return;
    }

    this.saving = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.careerService
      .createJob(this.newJob)
      .subscribe({

        next: () => {

          this.saving = false;

          this.showAddJob = false;

          this.successMessage =
            'Job added successfully.';

          this.loadJobs();
        },

        error: (error) => {

          console.error(
            'Create job failed:',
            error
          );

          this.saving = false;

          this.errorMessage =
            'Unable to create the job.';
        }
      });
  }

  deleteJob(job: any): void {

    if (!this.isAdmin) {
      return;
    }

    const jobId =
      job.jobId ??
      job.id;

    if (!jobId) {
      return;
    }

    const confirmed =
      window.confirm(
        `Delete "${job.title}"?`
      );

    if (!confirmed) {
      return;
    }

    this.careerService
      .deleteJob(jobId)
      .subscribe({

        next: () => {

          this.successMessage =
            'Job deleted successfully.';

          this.loadJobs();
        },

        error: (error) => {

          console.error(
            'Delete job failed:',
            error
          );

          this.errorMessage =
            'Unable to delete the job.';
        }
      });
  }
}