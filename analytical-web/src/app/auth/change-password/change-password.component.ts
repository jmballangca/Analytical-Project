import { Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
  ValidationErrors,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
})
export class ChangePasswordComponent {
  passwordForm$: FormGroup;
  error: string | null = null; // Error message property

  constructor(
    public activeModal: NgbActiveModal,
    private authService: AuthService,
    private toastr: ToastrService,
    private fb: FormBuilder
  ) {
    this.passwordForm$ = this.fb.nonNullable.group(
      {
        oldPassword: ['', [Validators.required]],
        newPassword: ['', [Validators.required, Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]],
      },
      { validators: this.passwordsMatchValidator }
    );
  }

  passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const newPassword = control.get('newPassword')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return newPassword === confirmPassword ? null : { passwordsMismatch: true };
  }

  async changePassword() {
    if (this.passwordForm$.invalid) {
      this.toastr.error('Please correct the errors before submitting.');
      return;
    }

    const { oldPassword, newPassword } = this.passwordForm$.value;
    try {
      await this.authService.changePassword(oldPassword, newPassword);
      this.toastr.success('Password changed successfully!');
      this.activeModal.close();
    } catch (err: any) {
      this.error = err['message'] || 'Error changing password.';
    }
  }
}
