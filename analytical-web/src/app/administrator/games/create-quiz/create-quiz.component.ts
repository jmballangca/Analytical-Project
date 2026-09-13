import { Component, inject } from '@angular/core';
import { QuizService } from '../../../services/quiz.service';

import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Quiz } from '../../../models/quiz/Quiz';

@Component({
  selector: 'app-create-quiz',
  templateUrl: './create-quiz.component.html',
  styleUrl: './create-quiz.component.css',
})
export class CreateQuizComponent {
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

      category: ['QUIZ_GAME', Validators.required],
      levels: [5, Validators.required],
    });
  }

  onSelectImage(event: any) {
    this.FOR_UPLOAD = event.target.files[0];
    // if (event.target.files) {
    //   let reader = new FileReader();
    //   reader.readAsDataURL(event.target.files[0]);
    //   reader.onload = (e: any) => {
    //     this.PRODUCT_IMAGE = e.target.result;
    //   };
    // }
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
        id: '',
        title: this.quizForm$.get('title')?.value ?? '',
        desc: this.quizForm$.get('desc')?.value ?? '',
        cover_photo: '',
        subject: subject,
        category: category,
        levels: levels,
        createdAt: new Date(),
        visible: false,
        schoolLevel: 'SHS',
      };

      if (this.FOR_UPLOAD !== null) {
        this.uploadFile(this.FOR_UPLOAD, quiz);
      } else {
        this.saveQuiz(quiz);
      }
    }
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
