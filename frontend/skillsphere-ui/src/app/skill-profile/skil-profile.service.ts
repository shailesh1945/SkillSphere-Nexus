import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class SkillProfileService {

  private http = inject(HttpClient);

  private readonly profileUrl =
    `${environment.apiUrl}/api/skill-profiles`;

  private readonly employeeUrl =
    `${environment.apiUrl}/api/employees`;

  private readonly catalogUrl =
    `${environment.apiUrl}/api/skills/catalog`;


  // =========================
  // CURRENT EMPLOYEE
  // =========================

  getCurrentEmployee() {
    return this.http.get<any>(
      `${this.employeeUrl}/me`
    );
  }


  // =========================
  // EMPLOYEE SKILL PROFILE
  // =========================

  getProfile(employeeId: string) {
    return this.http.get<any>(
      `${this.profileUrl}/${employeeId}`
    );
  }


  // =========================
  // ORGANIZATIONAL SKILL CATALOG
  // =========================

  getCatalog() {
    return this.http.get<any[]>(
      this.catalogUrl
    );
  }


  // =========================
  // ADD SKILL TO EMPLOYEE
  // =========================

  addSkill(
    employeeId: string,
    skillId: string,
    proficiency: number
  ) {

    return this.http.post<any>(
      `${this.profileUrl}/${employeeId}/skills`,
      {
        employeeId,
        skillId,
        proficiency
      }
    );
  }
}