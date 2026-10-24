import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import {
  CreateSkillRequest,
  PageResponse,
  Skill,
  SkillCategory,
  SkillQueryParams,
  SkillSuggestion,
  UpdateSkillRequest,
} from '../../shared/models/skill.model';

@Injectable({
  providedIn: 'root',
})
export class SkillApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/v1/skills';

  getSkills(filters: SkillQueryParams = {}): Observable<PageResponse<Skill>> {
    let params = new HttpParams();

    if (filters.search?.trim()) {
      params = params.set('search', filters.search.trim());
    }

    if (filters.category) {
      params = params.set('category', filters.category);
    }

    if (filters.page !== undefined) {
      params = params.set('page', filters.page);
    }

    if (filters.size !== undefined) {
      params = params.set('size', filters.size);
    }

    if (filters.sortBy) {
      params = params.set('sortBy', filters.sortBy);
    }

    if (filters.direction) {
      params = params.set('direction', filters.direction);
    }

    return this.http.get<PageResponse<Skill>>(this.apiUrl, { params });
  }

  getSkillById(id: string): Observable<Skill> {
    return this.http.get<Skill>(`${this.apiUrl}/${id}`);
  }

  getSuggestions(query: string): Observable<SkillSuggestion[]> {
    return this.http.get<SkillSuggestion[]>(`${this.apiUrl}/suggestions`, { params: { q: query } });
  }

  createSkill(request: CreateSkillRequest): Observable<Skill> {
    return this.http.post<Skill>(this.apiUrl, request);
  }

  updateSkill(id: string, request: UpdateSkillRequest): Observable<Skill> {
    return this.http.put<Skill>(`${this.apiUrl}/${id}`, request);
  }

  deleteSkill(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
