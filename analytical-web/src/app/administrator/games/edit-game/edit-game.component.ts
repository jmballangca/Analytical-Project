import { Component, inject, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { QuizService } from '../../../services/quiz.service';
import { Quiz } from '../../../models/quiz/Quiz';

@Component({
  selector: 'app-edit-game',
  templateUrl: './edit-game.component.html',
  styleUrl: './edit-game.component.css',
})
export class EditGameComponent {
  @Input() quiz!: Quiz;
  FOR_UPLOAD: any = null;
  activeModal = inject(NgbActiveModal);

  quizForm$: FormGroup;

  constructor(
    private fb: FormBuilder,
    private quizService: QuizService,
    private toastr: ToastrService
  ) {
    this.quizForm$ = fb.group({
      cover: [null],
      title: ['', Validators.required],
      desc: ['', Validators.required],
      subject: ['MATH', Validators.required],
      schoolLevel: ['JHS', Validators.required],
      category: ['REBUS_PUZZLE', Validators.required],
      levels: [5, Validators.required],
    });
  }

  async uploadFile(file: File, quiz: Quiz) {
    try {
      const result = await this.quizService.uploadFile(file);
      quiz.cover_photo = result;
      this.saveQuiz(quiz);
    } catch (err: any) {
      alert(err['message']);
    }
  }
  onSubmit() {
    if (this.quizForm$.valid) {
      const subject: 'MATH' | 'ENGLISH' =
        this.quizForm$.get('subject')?.value ?? 'MATH';
      const category:
        | 'PUZZLE_GAME'
        | 'MEMORY_GAME'
        | 'QUIZ_GAME'
        | 'MATH_GAME' = this.quizForm$.get('category')?.value ?? 'PUZZLE_GAME';
      const levels: number = +this.quizForm$.get('levels')?.value || 10;
      let quiz: Quiz = {
        id: this.quiz.id,
        title: this.quizForm$.get('title')?.value ?? this.quiz.title,
        desc: this.quizForm$.get('desc')?.value ?? '',
        cover_photo: this.quiz.cover_photo || '',
        subject: subject,
        category: category,
        levels: levels,
        createdAt: this.quiz.createdAt || new Date(),
        visible: this.quiz.visible || false,
        schoolLevel: this.quizForm$.get('schoolLevel')?.value ?? 'GRADE_11',
      };

      if (this.FOR_UPLOAD !== null) {
        this.uploadFile(this.FOR_UPLOAD, quiz);
      } else {
        this.saveQuiz(quiz);
      }
    }
  }
  saveQuiz(quiz: Quiz) {
    this.quizService
      .createQuiz(quiz)
      .then((data) => {
        this.toastr.success('Successfully Created');
        this.activeModal.close(data);
      })
      .catch((err) => this.toastr.success(err['message']));
  }
}
