import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { QuestionService } from '../../../services/question.service';

import { combineLatest, map, Observable, of, Subscription } from 'rxjs';
import { QuizService } from '../../../services/quiz.service';
import { Location } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { QuizWithQuestions } from '../../../models/quiz/QuizWithQuestions';
import { ToastrService } from 'ngx-toastr';
import { Quiz } from '../../../models/quiz/Quiz';
import { Levels } from '../../../models/quiz/Levels';
import { CreateQuestionComponent } from '../../questions/create-question/create-question.component';
import { LevelsService } from '../../../services/levels.service';
import { Questions } from '../../../models/Questions';
import { StudentWithSubmissions } from '../../../models/students/StudentWithSubmissions';
import { SubmissionsService } from '../../../services/submissions.service';
import { SubmissionWithStudent } from '../../../models/submissions/SubmissionWithStudent';
import { DeleteConfirmationComponent } from '../delete-confirmation/delete-confirmation.component';
import { CreateMemoryQuestionComponent } from '../../questions/create-memory-question/create-memory-question.component';

export interface QuestionsWithLevels {
  question: Questions;
  levelName: string;
}
@Component({
  selector: 'app-view-quiz',
  templateUrl: './view-quiz.component.html',
  styleUrl: './view-quiz.component.css',
})
export class ViewQuizComponent implements OnInit {
  private modalService = inject(NgbModal);
  quizId: string | null = null;
  quiz$: Observable<Quiz> | undefined;
  levels$: Observable<Levels[]> | undefined;
  questions$: Observable<QuestionsWithLevels[]> | undefined;
  submissionsWithStudent$: Observable<SubmissionWithStudent[]> = of([]);
  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private levelService: LevelsService,
    private location: Location,
    private toastr: ToastrService,
    private questionService: QuestionService,
    private submissionService: SubmissionsService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.quizId = params['id'] ?? null;
      if (this.quizId !== null) {
        this.submissionsWithStudent$ = this.submissionService
          .getSubmissionsByQuiz(this.quizId)
          .pipe(map((data) => data || []));
        this.quiz$ = this.quizService.getQuizById(this.quizId);
        this.levels$ = this.levelService.getLevels(this.quizId);
        this.questions$ = combineLatest([
          this.questionService.getAllQuestionByGameID(this.quizId),
          this.levels$,
        ]).pipe(
          map(([questions, levels]) => {
            return questions.map((question) => {
              const level = levels.find(
                (level) => level.id === question.levelID
              );
              return {
                question: question,
                levelName: level ? level.name : 'Unknown Level',
              };
            });
          })
        );
      }
    });
  }

  deleteQuiz(quiz: Quiz) {
    const modal = this.modalService.open(DeleteConfirmationComponent);
    modal.componentInstance.message = `Are you sure you want to delete
    ${quiz.title} ?`;
    modal.result.then((data) => {
      if (data === 'YES') {
        this.quizService
          .deleteQuiz(quiz.id, quiz.cover_photo)
          .then((data) => this.toastr.success('Successfully Deleted'))
          .catch((err) => this.toastr.error(err['message']))
          .finally(() => this.location.back());
      }
    });
  }

  showDialog(category: Questions['type']) {
    if (category === 'MEMORY_GAME') {
    }
  }

  getLevelName(levelID: string): Observable<string> {
    return (
      this.levels$?.pipe(
        map((levels) => {
          const level = levels.find((level) => level.id === levelID);
          return level ? level.name : 'Unknown Level';
        })
      ) ?? of('Unknown Level')
    );
  }
}
