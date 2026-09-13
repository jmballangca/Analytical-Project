import { Component, inject, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
  Observable,
  Subscription,
  debounceTime,
  distinctUntilChanged,
  map,
  of,
  switchMap,
} from 'rxjs';

import { QuizService } from '../../services/quiz.service';
import { CreateQuizComponent } from './create-quiz/create-quiz.component';
import { QuestionService } from '../../services/question.service';
import { ToastrService } from 'ngx-toastr';
import { Administrators } from '../../models/Administrator';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Quiz } from '../../models/quiz/Quiz';

@Component({
  selector: 'app-games',
  templateUrl: './games.component.html',
  styleUrl: './games.component.css',
})
export class GamesComponent implements OnInit {
  private modalService = inject(NgbModal);
  quiz$: Quiz[] = [];
  filteredQuiz$: Quiz[] = [];
  searchText = new FormControl('');

  admin$: Administrators | null = null;
  constructor(
    private quizService: QuizService,
    private questionService: QuestionService,
    private toastr: ToastrService,
    private authService: AuthService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.authService.administrator$.subscribe((data) => {
      this.admin$ = data;
    });

    this.quizService.getAllQuiz().subscribe((data) => {
      this.quiz$ = data;
      this.updatedFilteredGames();
    });
    this.searchText.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe(() => {
        this.updatedFilteredGames();
      });
  }
  updatedFilteredGames(): void {
    const searchText = this.searchText.value?.toLowerCase() ?? '';
    this.filteredQuiz$ = this.quiz$.filter((game) => {
      return game.title.toLowerCase().includes(searchText);
    });
  }
  createQuiz() {
    const modalRef = this.modalService.open(CreateQuizComponent, {
      size: 'lg',
    });
    modalRef.result.then((data: string | null) => {
      if (data !== null) {
        this.viewGame(data);
      }
    });
  }

  viewGame(id: string) {
    this.router.navigate(['main/games/view', id]);
  }

  filterByCategory(type: Quiz['category']): number {
    let count = 0;
    this.quiz$.forEach((e) => {
      if (e.category === type) {
        count += 1;
      }
    });
    return count;
  }
  // quizSize(type: Quiz['category']): Observable<number> {
  //   return this.quiz$.pipe(
  //     map((quizzes) => quizzes.filter((quiz) => quiz.category === type).length)
  //   );
  // }
}
