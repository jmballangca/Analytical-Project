import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-delete-confirmation',
  templateUrl: './delete-confirmation.component.html',
  styleUrl: './delete-confirmation.component.css',
})
export class DeleteConfirmationComponent {
  activeModal = inject(NgbActiveModal);
  @Input() message: string = '';

  confirmDelete() {
    this.activeModal.close('YES');
  }

  cancel() {
    this.activeModal.close();
  }
}
