import { Component, inject, Input, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { NIL } from 'uuid';
import { Administrators } from '../../models/Administrator';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css',
})
export class EditProfileComponent implements OnInit {
  activeModal = inject(NgbActiveModal);

  @Input() name: string = '';
  @Input() id: string = '';

  nameForm$: FormGroup = new FormGroup({
    name: new FormControl('', Validators.required),
  });

  constructor(
    private authService: AuthService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.nameForm$.patchValue({ name: this.name });
  }

  onSubmit() {
    const name = this.nameForm$.get('name')?.value ?? '';
    this.authService
      .editAdminName(this.id, name)
      .then(() => this.toastr.success('Successfully updated!'))
      .catch((err) => this.toastr.error(err.message))
      .finally(() => {
        this.nameForm$.reset();
        this.activeModal.close();
      });
  }
}
