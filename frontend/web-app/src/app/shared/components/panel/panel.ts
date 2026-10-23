import { Component, input } from '@angular/core';

@Component({
  selector: 'app-panel',
  imports: [],
  templateUrl: './panel.html',
  styleUrl: './panel.scss',
})
export class PanelComponent {
  readonly title = input<string | null>(null);
  readonly description = input<string | null>(null);
  readonly maxWidth = input<string>('var(--panel-max-width)');
}
