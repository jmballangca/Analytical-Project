import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { SubmissionsService } from '../../services/submissions.service';
import { QuizService } from '../../services/quiz.service';
import { StudentsService } from '../../services/students.service';
import { Students } from '../../models/students/Student';
import { displayFullname } from '../../utils/Constants';
import { map } from 'rxjs';
import { StudentWithSubmissions } from '../../models/students/StudentWithSubmissions';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-leaderboard',
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.css',
})
export class LeaderboardComponent {
  studentWithSubmissions: StudentWithSubmissions[] = [];
  paginatedStudents: StudentWithSubmissions[] = [];
  searchControl: FormControl = new FormControl('');
  currentPage = 1;
  pageSize = 10;

  constructor(private submissionService: SubmissionsService) {}

  ngOnInit(): void {
    this.submissionService.getSttudentWithSubmissions().subscribe((data) => {
      this.studentWithSubmissions = data;
      this.updatePaginatedStudents();
    });

    this.searchControl.valueChanges.subscribe(() => {
      this.updatePaginatedStudents();
    });
  }

  updatePaginatedStudents(): void {
    const searchTerm = this.searchControl.value.toLowerCase();
    const filteredStudents = this.studentWithSubmissions.filter((student) =>
      `${student.student.fname.toLowerCase()} ${student.student.lname.toLowerCase()}`.includes(
        searchTerm
      )
    );

    const startItem = (this.currentPage - 1) * this.pageSize;
    const endItem = startItem + this.pageSize;
    this.paginatedStudents = filteredStudents.slice(startItem, endItem);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.updatePaginatedStudents();
  }
  displayFullname(student: Students | null): string {
    if (student) {
      return `${student.fname} ${student.lname}`;
    }
    return 'No student information available';
  }
}
