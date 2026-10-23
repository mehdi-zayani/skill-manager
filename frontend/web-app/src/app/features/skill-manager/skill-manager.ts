import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, of } from 'rxjs';
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  finalize,
  map,
  switchMap,
} from 'rxjs/operators';

import { SkillApiService } from '../../core/services/skill-api.service';
import { ButtonComponent } from '../../shared/components/button/button';
import { PanelComponent } from '../../shared/components/panel/panel';
import { SkillBadgeComponent } from '../../shared/components/skill-badge/skill-badge';
import {
  CreateSkillRequest,
  Skill,
  SkillCategory,
  SkillSuggestion,
} from '../../shared/models/skill.model';

@Component({
  selector: 'app-skill-manager',
  imports: [FormsModule, ButtonComponent, PanelComponent, SkillBadgeComponent],
  templateUrl: './skill-manager.html',
  styleUrl: './skill-manager.scss',
})
export class SkillManager implements OnInit {
  private readonly skillApi = inject(SkillApiService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly searchTerms = new Subject<string>();

  readonly categories: SkillCategory[] = [
    'LANGUAGE',
    'FRAMEWORK',
    'LIBRARY',
    'DATABASE',
    'TOOL',
    'CLOUD',
    'DEVOPS',
    'TESTING',
    'SECURITY',
    'PLATFORM',
    'API',
    'MONITORING',
    'ARCHITECTURE',
    'METHODOLOGY',
    'OTHER',
  ];

  skills: Skill[] = [];
  suggestions: SkillSuggestion[] = [];

  name = '';
  category: SkillCategory = 'LANGUAGE';

  isLoading = false;
  isSearching = false;
  isSubmitting = false;
  showCatalog = false;
  deleteMode = false;
  selectedExistingSkill = false;

  errorMessage = '';
  successMessage = '';

  ngOnInit(): void {
    this.searchTerms
      .pipe(
        debounceTime(120),
        distinctUntilChanged(),
        switchMap((term) => {
          if (!term) {
            this.isSearching = false;
            return of([] as SkillSuggestion[]);
          }

          this.isSearching = true;
          this.cdr.markForCheck();

          return this.skillApi
            .getSkills({
              search: term,
              page: 0,
              size: 8,
              sortBy: 'name',
              direction: 'asc',
            })
            .pipe(
              map((response) =>
                response.items.map((skill): SkillSuggestion => ({
                  id: skill.id,
                  name: skill.name,
                  category: skill.category,
                })),
              ),
              catchError(() => of([] as SkillSuggestion[])),
              finalize(() => {
                this.isSearching = false;
                this.cdr.markForCheck();
              }),
            );
        }),
      )
      .subscribe((suggestions) => {
        this.suggestions = suggestions;
        this.cdr.markForCheck();
      });
  }

  onNameChange(value: string): void {
    this.name = value;
    this.selectedExistingSkill = false;
    this.errorMessage = '';
    this.successMessage = '';

    this.searchTerms.next(value.trim());
  }

  selectSuggestion(suggestion: SkillSuggestion): void {
    this.name = suggestion.name;
    this.category = suggestion.category;
    this.selectedExistingSkill = true;
    this.suggestions = [];
    this.cdr.markForCheck();
  }

  get alreadyExists(): boolean {
    const normalizedName = this.name.trim().toLowerCase();

    return (
      normalizedName.length > 0 &&
      (this.selectedExistingSkill ||
        this.suggestions.some((skill) => skill.name.trim().toLowerCase() === normalizedName))
    );
  }

  toggleCatalog(): void {
    this.showCatalog = !this.showCatalog;
    this.deleteMode = false;

    if (this.showCatalog) {
      this.loadSkills();
    }
  }

  loadSkills(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.markForCheck();

    this.skillApi
      .getSkills({
        page: 0,
        size: 100,
        sortBy: 'name',
        direction: 'asc',
      })
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: (response) => {
          this.skills = response.items;
          this.cdr.markForCheck();
        },
        error: () => {
          this.errorMessage = 'Unable to load skills. Make sure the backend is running.';
          this.cdr.markForCheck();
        },
      });
  }

  addSkill(): void {
    const trimmedName = this.name.trim();

    if (!trimmedName || this.isSubmitting || this.alreadyExists) {
      return;
    }

    const request: CreateSkillRequest = {
      name: trimmedName,
      category: this.category,
    };

    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.skillApi
      .createSkill(request)
      .pipe(
        finalize(() => {
          this.isSubmitting = false;
          this.cdr.markForCheck();
        }),
      )
      .subscribe({
        next: () => {
          this.name = '';
          this.category = 'LANGUAGE';
          this.suggestions = [];
          this.selectedExistingSkill = false;
          this.searchTerms.next('');
          this.successMessage = 'Skill added successfully.';

          if (this.showCatalog) {
            this.loadSkills();
          }

          this.cdr.markForCheck();
        },
        error: (error) => {
          this.errorMessage =
            error.status === 409
              ? 'This skill already exists in the catalog.'
              : 'Unable to add this skill. Please try again.';
          this.cdr.markForCheck();
        },
      });
  }

  removeSkill(id: string): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.skillApi.deleteSkill(id).subscribe({
      next: () => {
        this.skills = this.skills.filter((skill) => skill.id !== id);
        this.successMessage = 'Skill deleted successfully.';
        this.cdr.markForCheck();
      },
      error: () => {
        this.errorMessage = 'Unable to delete this skill.';
        this.cdr.markForCheck();
      },
    });
  }

  toggleDeleteMode(): void {
    this.deleteMode = !this.deleteMode;
  }

  categoryLabel(category: SkillCategory): string {
    return category.charAt(0) + category.slice(1).toLowerCase();
  }
}
