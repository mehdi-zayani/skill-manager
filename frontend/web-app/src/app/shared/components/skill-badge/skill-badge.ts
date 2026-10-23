
import { Component, input, output } from '@angular/core';

import { Skill, SkillSuggestion } from '../../models/skill.model';

@Component({
  selector: 'app-skill-badge',
  imports: [],
  templateUrl: './skill-badge.html',
  styleUrl: './skill-badge.scss',
})
export class SkillBadgeComponent {
  readonly skill = input.required<Skill | SkillSuggestion>();
  readonly removable = input(false);

  readonly remove = output<string>();
}
