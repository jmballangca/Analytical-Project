import { Component, inject, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { StudentsService } from '../../services/students.service';
import { Students } from '../../models/students/Student';
import { displayFullname } from '../../utils/Constants';
import { FormControl } from '@angular/forms';
import {
  debounceTime,
  switchMap,
  map,
  distinctUntilChanged,
  Observable,
} from 'rxjs';

@Component({
  selector: 'app-students',
  templateUrl: './students.component.html',
  styleUrl: './students.component.css',
})
export class StudentsComponent implements OnInit {
  students$: Students[] = [];
  filteredStudents$: Students[] = [];
  searchText$ = new FormControl('');

  constructor(private studentService: StudentsService) {}

  ngOnInit(): void {
    this.studentService.getAllStudent().subscribe((data) => {
      this.students$ = data;
      this.updateStudents();
    });
    // Subscribe to search text changes
    this.searchText$.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe(() => {
        this.updateStudents();
      });
  }

  updateStudents(): void {
    const searchText = this.searchText$.value?.toLowerCase() ?? '';
    this.filteredStudents$ = this.students$.filter((student) => {
      const fullname =
        student.fname + ' ' + student.mname + ' ' + student.lname;
      return fullname.toLowerCase().includes(searchText); // Filter based on the full name
    });
  }

  // Method to display the full name
  displayFullname(student: Students): string {
    return student.fname + ' ' + student.mname + ' ' + student.lname;
  }
}
