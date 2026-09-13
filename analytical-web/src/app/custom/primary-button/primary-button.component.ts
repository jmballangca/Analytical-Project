import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-primary-button',
  templateUrl: './primary-button.component.html',
  styleUrl: './primary-button.component.css',
})
export class PrimaryButtonComponent {
  @Input() isLoading = false;
  @Input() title = 'Submit';
  @Output() event: EventEmitter<any> = new EventEmitter();
}
