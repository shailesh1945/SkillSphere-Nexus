import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class AssessmentService {

  private http = inject(HttpClient);

  private readonly baseUrl =
    `${environment.apiUrl}/api/assessments`;

  // =========================================================
  // HR / TRAINING MANAGER
  // =========================================================

  createAssessment(data: any) {
    return this.http.post<any>(
      this.baseUrl,
      data
    );
  }

  updateAssessment(
    assessmentId: string,
    data: any
  ) {
    return this.http.put<any>(
      `${this.baseUrl}/${assessmentId}`,
      data
    );
  }

  getAssessment(
    assessmentId: string
  ) {
    return this.http.get<any>(
      `${this.baseUrl}/${assessmentId}`
    );
  }

  getAssessments() {
    return this.http.get<any[]>(
      this.baseUrl
    );
  }

  getEmployeeAssessments(
    employeeId: string
  ) {
    return this.http.get<any[]>(
      `${this.baseUrl}/employee/${employeeId}`
    );
  }

  getSkillAssessments(
    skillId: string
  ) {
    return this.http.get<any[]>(
      `${this.baseUrl}/skill/${skillId}`
    );
  }

  getPassedAssessments() {
    return this.http.get<any[]>(
      `${this.baseUrl}/passed`
    );
  }

  getFailedAssessments() {
    return this.http.get<any[]>(
      `${this.baseUrl}/failed`
    );
  }

  verifyAssessment(
    assessmentId: string
  ) {
    return this.http.put<any>(
      `${this.baseUrl}/${assessmentId}/verify`,
      null
    );
  }

  deleteAssessment(
    assessmentId: string
  ) {
    return this.http.delete<void>(
      `${this.baseUrl}/${assessmentId}`
    );
  }

  // =========================================================
  // EMPLOYEE
  // =========================================================

  getMyAssessments() {
    return this.http.get<any[]>(
      `${this.baseUrl}/my`
    );
  }

  getAssessmentQuestions(
    assessmentId: string
  ) {
    return this.http.get<any[]>(
      `${this.baseUrl}/${assessmentId}/questions`
    );
  }

  submitAssessment(
    assessmentId: string,
    answers: any[]
  ) {
    return this.http.post<any>(
      `${this.baseUrl}/${assessmentId}/submit`,
      {
        answers
      }
    );
  }
}