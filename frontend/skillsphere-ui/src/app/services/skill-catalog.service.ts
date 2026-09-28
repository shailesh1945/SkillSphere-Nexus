import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class SkillCatalogService {

  private http = inject(HttpClient);

  private readonly baseUrl =
    `${environment.apiUrl}/api/skills/catalog`;

  getSkills() {
    return this.http.get<any[]>(
      this.baseUrl
    );
  }

  getSkill(skillId: string) {
    return this.http.get<any>(
      `${this.baseUrl}/${skillId}`
    );
  }

  addSkill(skill: any) {
    return this.http.post<any>(
      this.baseUrl,
      skill
    );
  }

  updateSkill(
    skillId: string,
    skill: any
  ) {
    return this.http.put<any>(
      `${this.baseUrl}/${skillId}`,
      skill
    );
  }

  deleteSkill(skillId: string) {
    return this.http.delete<void>(
      `${this.baseUrl}/${skillId}`
    );
  }

  getByCategory(category: string) {
    return this.http.get<any[]>(
      `${this.baseUrl}/category/${category}`
    );
  }

  searchSkills(keyword: string) {
    return this.http.get<any[]>(
      `${this.baseUrl}/search`,
      {
        params: { keyword }
      }
    );
  }
}