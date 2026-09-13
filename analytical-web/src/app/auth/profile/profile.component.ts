import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Administrators } from '../../models/Administrator';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EditProfileComponent } from '../edit-profile/edit-profile.component';
import { ChangePasswordComponent } from '../change-password/change-password.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  modalService = inject(NgbModal);
  admin$: Administrators | null = null;
  constructor(private authService: AuthService) {}
  ngOnInit(): void {
    this.authService.administrator$.subscribe((data) => {
      this.admin$ = data;
      console.log(this.admin$);
    });
  }

  logout() {
    this.authService.logout();
  }

  editProfile() {
    const modal = this.modalService.open(EditProfileComponent);
    modal.componentInstance.name = this.admin$?.name;
    modal.componentInstance.id = this.admin$?.id;
  }

  changePassword() {
    const modal = this.modalService.open(ChangePasswordComponent);
  }
}
