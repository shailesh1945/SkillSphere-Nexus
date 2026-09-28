import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private http = inject(HttpClient);

  private readonly baseUrl = `${environment.apiUrl}/api/employees`;

  addEmployee(employee: any) {
    return this.http.post<any>(this.baseUrl, employee);
  }


  getCurrentEmployee() {
  return this.http.get<any>(
    `${environment.apiUrl}/api/employees/me`
  );
}


  getMyProfile() {
    return this.http.get<any>(`${this.baseUrl}/me`);
  }

  getEmployees() {
    return this.http.get<any[]>(this.baseUrl);
  }

  getEmployee(empId: string) {
    return this.http.get<any>(`${this.baseUrl}/${empId}`);
  }

  updateEmployee(employeeId: string, employee: any) {
    return this.http.put<any>(`${this.baseUrl}/${employeeId}`, employee);
  }

  deleteEmployee(employeeId: string) {
    return this.http.delete<void>(`${this.baseUrl}/${employeeId}`);
  }
}
