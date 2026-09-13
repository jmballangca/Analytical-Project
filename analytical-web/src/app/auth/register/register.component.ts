import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Administrators, AdminType } from '../../models/Administrator';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private authService: AuthService
  ) {
    this.registerForm = this.fb.group(
      {
        name: ['', [Validators.required, Validators.minLength(3)]],
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.minLength(6)]],
        confirmPassword: ['', [Validators.required]],
      },
      {
        validators: this.passwordMatchValidator,
      }
    );
  }

  // Custom validator to check if password and confirm password match
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password')?.value;
    const confirmPassword = form.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { mismatch: true };
  }

  // Handle form submission
  onSubmit() {
    // Check if form is invalid
    if (this.registerForm.invalid) {
      this.toastr.error('Please fill out the form correctly.');
      return;
    }

    this.isLoading = true;

    // Get form values
    const formValues = this.registerForm.value;

    let admin: Administrators = {
      id: '',
      name: formValues.name,
      email: formValues.email,
      profile: '',
      type: AdminType.TERACHER,
    };

    this.authService
      .register(admin, formValues.password)
      .then(() => {
        this.toastr.success('Teacher registered successfully.');
        this.isLoading = false;
      })
      .catch((err: any) => {
        this.toastr.error(
          err.message || 'An error occurred during registration.'
        );
        this.isLoading = false;
      });
  }
}
