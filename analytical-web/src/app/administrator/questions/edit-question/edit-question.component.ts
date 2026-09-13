import { Component, inject, Input, ViewChild } from '@angular/core';
import { Questions } from '../../../models/Questions';
import { NgbActiveModal, NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { Levels } from '../../../models/quiz/Levels';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {
  Subject,
  OperatorFunction,
  Observable,
  debounceTime,
  distinctUntilChanged,
  filter,
  merge,
  map,
} from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { QuestionService } from '../../../services/question.service';

@Component({
  selector: 'app-edit-question',
  templateUrl: './edit-question.component.html',
  styleUrl: './edit-question.component.css',
})
export class EditQuestionComponent {
  @Input() question!: Questions;

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
      question: this.question.question,
      levelID: this.question.levelID,
      hint: this.question.hint,
      answer: this.question.answer,
    });
    this.choices$ = this.question.choices;
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
      ...this.question,
      levelID: this.questionForm$.get('levelID')?.value ?? '',
      gameID: this.gameID,
      question: questions,
      answer: answer,
      choices: this.choices$,
      type: category,
      updatedAt: new Date(),
      hint: hint,
    };

    this.saveQuestion(question, this.FOR_UPLOAD);
  }

  saveQuestion(question: Questions, file: File) {
    this.questionService
      .updateQuestion(question, file)
      .then((data) => {
        this.toastr.success('successfully Updated!');
      })
      .catch((err) => this.toastr.error(err['message']))
      .finally(() => this.activeModal.close());
  }
}
