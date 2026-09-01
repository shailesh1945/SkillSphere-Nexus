import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class LearningService {

  private baseUrl = 'http://localhost:8084/api/learning'

  constructor(private http: HttpClient) {}

  // get all the courses
  getCourses() {
    return this.http.get<any[]>(`${this.baseUrl}/courses`);
  }

  // get courses by id
  getCourse(courseId: string) {
    return this.http.get<any[]>(
      `${this.baseUrl}/courses/${courseId}`);
  }

  getCourseEnrollments(courseId: string) {
  return this.http.get<any[]>(
    `${this.baseUrl}/enrollments/course/${courseId}`
  );
}


  // get active courses
   getActiveCourses() {
    return this.http.get<any[]>(
      `${this.baseUrl}/courses/active`
    );
  }


  // search course by title
  searchCourses(keyword: string) {
    return this.http.get<any[]>(
      `${this.baseUrl}/courses/search`,
      {
        params: { keyword }
      }
    );
  }

  // enroll 
  enroll(empId: string, courseId: string) {
    return this.http.post<any>(
      `${this.baseUrl}/enrollments`, 
      null, 
      { params: { empId, courseId } }
    );
  }

  // get enrollments by enrollment id
    getEnrollment(
    enrollmentId: string
  ){

    return this.http.get<any[]>(
      `${this.baseUrl}/enrollments/${enrollmentId}`
    );
  }


  // get enrollment by emp id
    getEnrollments(
    empId: string
  ){

    return this.http.get<any[]>(
      `${this.baseUrl}/enrollments/employee/${empId}`
    );
  }

  // update the progress
  updateProgress(enrollmentId: string, progress: number) {
    return this.http.put<any[]>(
      `${this.baseUrl}/progress/${enrollmentId}`, 
      null, 
      {params: { progress },
    });
  }

  // submit assessment
  submitAssessment(enrollmentId: string, score: number) {
    return this.http.post<any[]>(
      `${this.baseUrl}/progress/${enrollmentId}/assessment`, 
      null, 
      {params: { score },
    });
  }

  // mark course as complete
  completeCourse(enrollmentId: string) {
    return this.http.post<any[]>(
      `${this.baseUrl}/progress/${enrollmentId}/complete`, 
      null);
  }


  // generate certificate
   generateCertificate(
    enrollmentId: string
  ) {

    return this.http.post<any[]>(
      `${this.baseUrl}/certificates/${enrollmentId}`,
      null
    );
  }


  // create learning path
  createLearningPath(
    path: any
  ){

    return this.http.post<any[]>(
      `${this.baseUrl}/paths`,
      path
    );
  }


  // add course to pathway
  addCourseToPath(
    pathId: string,
    courseId: string,
    sequence: number
  ){

    return this.http.post<any[]>(
      `${this.baseUrl}/paths/${pathId}/courses/${courseId}`,
      null,
      {
        params: {
          sequence: sequence
        }
      }
    );
  }

  // get courses by path id
  getPathCourses(
    pathId: string
  ){

    return this.http.get<any[]>(
      `${this.baseUrl}/paths/${pathId}/courses`
    );
  }

}

