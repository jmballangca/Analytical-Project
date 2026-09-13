import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { QuestionService } from '../../services/question.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrl: './main.component.css',
})
export class MainComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
    private questionService: QuestionService,
    private toastr: ToastrService
  ) {}

  logout() {
    this.authService.logout().then(() => {
      this.toastr.success('Successfully logged out.');
      this.router.navigate(['']);
    });
  }
}
