export type SkillCategory =
  | 'LANGUAGE'
  | 'FRAMEWORK'
  | 'LIBRARY'
  | 'DATABASE'
  | 'TOOL'
  | 'CLOUD'
  | 'DEVOPS'
  | 'TESTING'
  | 'SECURITY'
  | 'PLATFORM'
  | 'API'
  | 'MONITORING'
  | 'ARCHITECTURE'
  | 'METHODOLOGY'
  | 'OTHER';

export interface Skill {
  id: string;
  name: string;
  normalizedName: string;
  category: SkillCategory;
  createdAt: string;
  updatedAt: string;
}

export interface SkillSuggestion {
  id: string;
  name: string;
  category: SkillCategory;
}

export interface CreateSkillRequest {
  name: string;
  category: SkillCategory;
}

export type UpdateSkillRequest = CreateSkillRequest;

export interface PageResponse<T> {
  items: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface SkillQueryParams {
  search?: string;
  category?: SkillCategory;
  page?: number;
  size?: number;
  sortBy?: 'name' | 'category' | 'createdAt' | 'updatedAt';
  direction?: 'asc' | 'desc';
}
