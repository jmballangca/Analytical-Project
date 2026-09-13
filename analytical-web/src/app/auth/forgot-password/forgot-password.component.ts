import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css',
})
export class ForgotPasswordComponent {
  activeModal = inject(NgbActiveModal);
  emailForm$: FormGroup;
  constructor(private fb: FormBuilder, private toastr: ToastrService) {
    this.emailForm$ = fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  onSubmit() {
    if (this.emailForm$.invalid) {
      this.toastr.error('Invalid Email');
      return;
    }
    this.activeModal.close(this.emailForm$.get('email')?.value ?? '');
  }
}
