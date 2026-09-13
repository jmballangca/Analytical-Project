import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { SubmissionWithStudent } from '../../../models/submissions/SubmissionWithStudent';
import { Students } from '../../../models/students/Student';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-submissions-table',
  templateUrl: './submissions-table.component.html',
  styleUrl: './submissions-table.component.css',
})
export class SubmissionsTableComponent implements OnChanges {
  @Input() submissions: SubmissionWithStudent[] = [];
  paginatedSubmissions: SubmissionWithStudent[] = [];
  currentPage = 1;
  pageSize = 10;
  searchControl: FormControl = new FormControl('');

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['submissions']) {
      this.updatePaginatedSubmissions();
    }
  }

  updatePaginatedSubmissions(): void {
    const searchTerm = this.searchControl.value.toLowerCase().trim(); // Trim whitespace
    const filteredSubmissions = this.submissions.filter((submission) =>
      `${submission.students?.fname.toLowerCase()} ${submission.students?.lname.toLowerCase()}`.includes(
        searchTerm
      )
    );

    const startItem = (this.currentPage - 1) * this.pageSize;
    const endItem = startItem + this.pageSize;
    this.paginatedSubmissions = filteredSubmissions.slice(startItem, endItem);

    console.log('Search Term:', searchTerm);
    console.log('Filtered Submissions:', filteredSubmissions);
    console.log('Paginated Submissions:', this.paginatedSubmissions);
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
