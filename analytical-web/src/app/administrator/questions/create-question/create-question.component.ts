import { Component, Input, OnInit, ViewChild, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal, NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';

import { QuizService } from '../../../services/quiz.service';
import { QuestionService } from '../../../services/question.service';
import { Questions } from '../../../models/Questions';
import { ToastrService } from 'ngx-toastr';
import { ImagePickerComponent } from '../../../custom/image-picker/image-picker.component';
import {
  debounceTime,
  distinctUntilChanged,
  filter,
  map,
  merge,
  Observable,
  OperatorFunction,
  Subject,
} from 'rxjs';
import { Levels } from '../../../models/quiz/Levels';
import { Category } from '../../../models/submissions/Category';

@Component({
  selector: 'app-create-question',
  templateUrl: './create-question.component.html',
  styleUrl: './create-question.component.css',
})
export class CreateQuestionComponent implements OnInit {
  FOR_UPLOAD: any = null;
  activeModal = inject(NgbActiveModal);
  questionForm$: FormGroup;

  @Input() levels: Levels[] = [];
  @Input() type!: Questions['type'];
  @Input() gameID!: string;
  choices$: string[] = [];

  @ViewChild('instance', { static: true }) instance: NgbTypeahead | undefined;

  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  search: OperatorFunction<string, readonly string[]> = (
    text$: Observable<string>
  ) => {
    const debouncedText$ = text$.pipe(
      debounceTime(200),
      distinctUntilChanged()
    );
    const clicksWithClosedPopup$ = this.click$.pipe(
      filter(() => !this.instance?.isPopupOpen())
    );
    const inputFocus$ = this.focus$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      map((term) =>
        (term === ''
          ? this.choices$
          : this.choices$.filter(
              (v) => v.toLowerCase().indexOf(term.toLowerCase()) > -1
            )
        ).slice(0, 10)
      )
    );
  };

  constructor(
    private fb: FormBuilder,
    private questionService: QuestionService,
    private toastr: ToastrService
  ) {
    this.questionForm$ = fb.group({
      question: [''],
      levelID: [null, Validators.required],
      hint: [''],
      answer: ['', Validators.required],
      category: [''],
      choices: [''],
    });
  }
  ngOnInit(): void {
    this.questionForm$.patchValue({
      category: this.type,
    });
  }
  addToChoices() {
    let currentChoice$: string = this.questionForm$.get('choices')?.value ?? '';
    console.log(currentChoice$);
    if (currentChoice$ === '') {
      this.toastr.error('Invalid choice');
      return;
    }
    if (this.choices$.includes(currentChoice$.toLowerCase())) {
      this.toastr.warning('Already exists!');
      return;
    }
    this.choices$.push(currentChoice$.toLowerCase());
    this.questionForm$.get('choices')?.setValue('');
  }

  removeChoice(choice: string) {
    const index = this.choices$.findIndex(
      (c) => c.toLowerCase() === choice.toLowerCase()
    );
    if (index !== -1) {
      this.choices$.splice(index, 1);
    } else {
      console.warn(`Choice "${choice}" not found in choices list.`);
    }
  }
  onSelectImage(event: any) {
    this.FOR_UPLOAD = event;
    // if (event.target.files) {
    //   let reader = new FileReader();
    //   reader.readAsDataURL(event.target.files[0]);
    //   reader.onload = (e: any) => {
    //     this.PRODUCT_IMAGE = e.target.result;
    //   };
    // }
  }

  onSubmit() {
    if (!this.questionForm$.valid) return;

    const answer = this.questionForm$.get('answer')?.value ?? '';
    const hint = this.questionForm$.get('hint')?.value ?? '';
    const questions = this.questionForm$.get('question')?.value ?? '';
    const category = this.type;

    const question: Questions = {
      id: '',
      levelID: this.questionForm$.get('levelID')?.value ?? '',
      gameID: this.gameID,
      question: questions,
      image: null,
      answer,
      choices: this.choices$,
      type: category,
      createdAt: new Date(),
      updatedAt: new Date(),
      hint: hint,
    };

    this.saveQuestion(question, this.FOR_UPLOAD);
  }

  saveQuestion(question: Questions, file: File) {
    this.questionService
      .createQuestion(question, file)
      .then((data) => {
        this.toastr.success('successfully added!');
      })
      .catch((err) => this.toastr.error(err['message']))
      .finally(() => this.activeModal.close());
  }
}
