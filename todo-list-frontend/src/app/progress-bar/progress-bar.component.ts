import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-progress-bar',
  template: `<div *ngIf="loading" class="progress-bar"></div>`,
  styleUrls: ['./progress-bar.component.scss']
})
export class ProgressBarComponent {
  @Input() loading: boolean = false;
}
