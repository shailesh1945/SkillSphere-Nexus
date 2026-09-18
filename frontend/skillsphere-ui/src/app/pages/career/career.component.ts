import { Component, OnInit } from '@angular/core';
import { CareerService } from '../../services/career.service';

@Component({
  selector: 'app-career-list',
  standalone: true,
  templateUrl: './career-list.component.html',
  styleUrl: './career-list.component.css'
})

@Component({
  selector: 'app-career',
  imports: [],
  templateUrl: './career.component.html',
  styleUrl: './career.component.scss'
})
export class CareerComponent {

  plans: any[] = [];

  constructor(
    private careerService: CareerService
  ) {}

  ngOnInit(): void {
    this.loadPlans();
  }

  loadPlans(): void {

    this.careerService
      .getCareerPlans()
      .subscribe({
        next: (data) => {
          this.plans = data;
        },

        error: (error) => {
          console.error(
            'Error loading career plans:',
            error
          );
        }
      });
  }

  trackByPlanId(
    index: number,
    plan: any
  ): string {
    return plan.planId;
  }
}
