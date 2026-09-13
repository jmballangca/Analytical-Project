import { Component, OnInit, inject } from '@angular/core';
import {
  QuestionService,
  QuestionsWithSettings,
} from '../../services/question.service';
import { Questions } from '../../models/Questions';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CreateQuestionComponent } from './create-question/create-question.component';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-questions',
  templateUrl: './questions.component.html',
  styleUrl: './questions.component.css',
})
export class QuestionsComponent implements OnInit {
  modalService = inject(NgbModal);

  questions$: Observable<Questions[]> | undefined;

  page = 1;
  pageSize = 10;
  collectionSize = 0;

  constructor(
    private questionsService: QuestionService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {}
  ngOnInit(): void {}

  // loadMoreQuestion(start: number) {
  //   this.questionsService.loadMoreQuestion(start).then((data) => {
  //     this.questions$ = data;
  //   });
  // }
  // loadPrevQuestions() {
  //   let questionID = this.questions$[0].id;
  //   console.log(questionID);
  //   this.questionsService.getPreviousQuestion(questionID).then((data) => {
  //     this.questions$ = data;
  //   });
  // }

  // createQuestion() {
  //   const modal = this.modalService.open(CreateQuestionComponent, {
  //     size: 'xl',
  //   });
  // }
}
