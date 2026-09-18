import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CareerService {

  private baseUrl = 'http://localhost:8083/api/career';

  constructor(private http: HttpClient) {}

  // Career Plans

  getCareerPlans() {
    return this.http.get<any[]>(
      `${this.baseUrl}/plans`
    );
  }

  getCareerPlanById(id: string) {
    return this.http.get<any>(
      `${this.baseUrl}/plans/${id}`
    );
  }

  getCareerPlansByEmployee(employeeId: string) {
    return this.http.get<any[]>(
      `${this.baseUrl}/plans/employee/${employeeId}`
    );
  }

  createCareerPlan(data: any) {
    return this.http.post<any>(
      `${this.baseUrl}/plans`,
      data
    );
  }

  updateCareerPlan(id: string, data: any) {
    return this.http.put<any>(
      `${this.baseUrl}/plans/${id}`,
      data
    );
  }

  deleteCareerPlan(id: string) {
    return this.http.delete<void>(
      `${this.baseUrl}/plans/${id}`
    );
  }


  // Jobs

  getJobs() {
    return this.http.get<any[]>(
      `${this.baseUrl}/jobs`
    );
  }

  getActiveJobs() {
    return this.http.get<any[]>(
      `${this.baseUrl}/jobs/active`
    );
  }

  createJob(data: any) {
    return this.http.post<any>(
      `${this.baseUrl}/jobs`,
      data
    );
  }

  deleteJob(id: string) {
    return this.http.delete<void>(
      `${this.baseUrl}/jobs/${id}`
    );
  }


  // Analytics

  getAnalytics() {
    return this.http.get<any>(
      `${this.baseUrl}/analytics`
    );
  }
}