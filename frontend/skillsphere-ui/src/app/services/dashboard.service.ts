import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, Observable, of, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private http = inject(HttpClient);

  private readonly baseUrl = 'http://localhost:8090';

  getDashboardData(): Observable<any> {

    return forkJoin({

      employees: this.http.get<any>(
        `${this.baseUrl}/api/employees`
      ).pipe(
        catchError(error => {
          console.error('Employees API failed:', error);
          return of([]);
        })
      ),

      skills: this.http.get<any>(
        `${this.baseUrl}/api/skills/catalog`
      ).pipe(
        catchError(error => {
          console.error('Skills API failed:', error);
          return of([]);
        })
      ),

      courses: this.http.get<any>(
        `${this.baseUrl}/api/learning/courses`
      ).pipe(
        catchError(error => {
          console.error('Courses API failed:', error);
          return of([]);
        })
      ),

      expiringCertifications: this.http.get<any>(
        `${this.baseUrl}/api/certifications/expiring`
      ).pipe(
        catchError(error => {
          console.error('Expiring certifications API failed:', error);
          return of([]);
        })
      ),

      careerPlans: this.http.get<any>(
        `${this.baseUrl}/api/career/plans`
      ).pipe(
        catchError(error => {
          console.error('Career plans API failed:', error);
          return of([]);
        })
      ),

      analytics: this.http.get<any>(
        `${this.baseUrl}/api/career/analytics`
      ).pipe(
        catchError(error => {
          console.error('Analytics API failed:', error);
          return of(null);
        })
      )

    }).pipe(

      map(data => {

        return {
          employees: this.getCount(data.employees),

          skills: this.getCount(data.skills),

          courses: this.getCount(data.courses),

          expiringCertifications:
            this.getCount(data.expiringCertifications),

          careerPlans:
            this.getCount(data.careerPlans),

          analytics: data.analytics
        };

      })

    );
  }

  private getCount(data: any): number {

    if (Array.isArray(data)) {
      return data.length;
    }

    if (data?.content && Array.isArray(data.content)) {
      return data.content.length;
    }

    if (typeof data === 'number') {
      return data;
    }

    if (typeof data?.count === 'number') {
      return data.count;
    }

    if (typeof data?.total === 'number') {
      return data.total;
    }

    return 0;
  }
}