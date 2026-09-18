import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../services/career.service';

@Component({
  selector: 'app-analytics',
  standalone: true,
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.css'
})
export class AnalyticsComponent implements OnInit {

  analytics: any = null;

  constructor(
    private careerService: CareerService
  ) {}

  ngOnInit(): void {

    this.careerService
      .getAnalytics()
      .subscribe({
        next: (data) => {
          this.analytics = data;
        },

        error: (error) => {
          console.error(
            'Error loading analytics:',
            error
          );
        }
      });
  }
}