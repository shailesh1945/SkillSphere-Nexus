import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../services/career.service';

@Component({
  selector: 'app-job-list',
  standalone: true,
  templateUrl: './job-list.component.html',
  styleUrl: './job-list.component.css'
})

export class JobsComponent {

   jobs: any[] = [];

  constructor(
    private careerService: CareerService
  ) {}

  ngOnInit(): void {

    this.careerService
      .getActiveJobs()
      .subscribe({
        next: (data) => {
          this.jobs = data;
        },

        error: (error) => {
          console.error(
            'Error loading jobs:',
            error
          );
        }
      });
  }
}
