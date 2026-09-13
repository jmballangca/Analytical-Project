import { Component, OnInit } from '@angular/core';
import { SubmissionsService } from '../../services/submissions.service';
import { Observable } from 'rxjs';
import { StudentWithSubmissions } from '../../models/students/StudentWithSubmissions';
import { SubmissionWithStudent } from '../../models/submissions/SubmissionWithStudent';
import { Students } from '../../models/students/Student';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-submissions',
  templateUrl: './submissions.component.html',
  styleUrl: './submissions.component.css',
})
export class SubmissionsComponent implements OnInit {
  submissions: SubmissionWithStudent[] = []; // assuming your Submission model
  currentPage = 1;
  pageSize = 20;
  paginatedSubmissions: SubmissionWithStudent[] = [];
  searchControl: FormControl = new FormControl('');
  constructor(private submissionService: SubmissionsService) {}
  get countSubmissionsLastWeek(): number {
    const oneWeekAgo = new Date();
    oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);

    return this.submissions.filter((submission) => {
      const submissionDate = new Date(submission.submission.createdAt);
      return submissionDate >= oneWeekAgo;
    }).length;
  }

  get countSubmissionsToday(): number {
    const today = new Date();
    const startOfDay = new Date(
      today.getFullYear(),
      today.getMonth(),
      today.getDate()
    );

    return this.submissions.filter((submission) => {
      const submissionDate = new Date(submission.submission.createdAt);
      return submissionDate >= startOfDay;
    }).length;
  }

  ngOnInit(): void {
    this.submissionService.getSubmissionWithStudent().subscribe((data) => {
      this.submissions = data;
      console.log(data);
      this.updatePaginatedSubmissions();
    });
    this.searchControl.valueChanges.subscribe(() => {
      this.updatePaginatedSubmissions();
    });
  }

  updatePaginatedSubmissions(): void {
    const searchTerm = this.searchControl.value.toLowerCase();
    const filteredSubmissions = this.submissions.filter((submission) => {
      const searchTerm = this.searchControl.value.toLowerCase();
      return (
        submission.submission.quizInfo?.name
          ?.toLowerCase()
          .includes(searchTerm) ||
        `${submission.students?.fname.toLowerCase()} ${submission.students?.lname.toLowerCase()}`.includes(
          searchTerm
        )
      );
    });
    const startItem = (this.currentPage - 1) * this.pageSize;
    const endItem = startItem + this.pageSize;

    this.paginatedSubmissions = filteredSubmissions.slice(startItem, endItem);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.updatePaginatedSubmissions();
  }

  displayFullname(student: Students | null): string {
    if (student) {
      return `${student.fname} ${student.lname}`;
    }
    return 'No student information available';
  }
}
