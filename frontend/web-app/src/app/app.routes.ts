
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/skill-manager/skill-manager').then(
        (module) => module.SkillManager,
      ),
  },
  {
    path: '**',
    redirectTo: '',
  },
];
