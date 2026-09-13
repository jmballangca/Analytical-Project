import { Component, inject, Input, OnInit } from '@angular/core';
import { Levels } from '../../../models/quiz/Levels';
import { Questions } from '../../../models/Questions';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  Validators,
} from '@angular/forms';
import { QuestionService } from '../../../services/question.service';
import { ToastrService } from 'ngx-toastr';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-create-memory-question',
  templateUrl: './create-memory-question.component.html',
  styleUrl: './create-memory-question.component.css',
})
export class CreateMemoryQuestionComponent implements OnInit {
  activeModal = inject(NgbActiveModal);
  @Input() gameID: string = '';
  @Input() levels: Levels[] = [];
  @Input() type!: Questions['type'];
  questionForm$: FormGroup;
  selectedImages: string[] = [];
  selectedImagesFilesForUpload: File[] = [];

  onFileSelect(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (input.files) {
      const files = Array.from(input.files);

      files.forEach((file) => {
        if (file.type.startsWith('image/')) {
          const reader = new FileReader();

          reader.onload = (e: ProgressEvent<FileReader>) => {
            if (e.target?.result) {
              this.selectedImages.push(e.target.result as string);
            }
          };

          reader.readAsDataURL(file);
          this.selectedImagesFilesForUpload.push(file);
        }
      });
    }
  }

  deleteImage(index: number): void {
    this.selectedImages.splice(index, 1);
    this.selectedImagesFilesForUpload.splice(index, 1);
  }
  constructor(
    private fb: FormBuilder,
    private questionService: QuestionService,
    private toastr: ToastrService
  ) {
    this.questionForm$ = fb.nonNullable.group({
      category: ['', Validators.required],
      levelID: [null, Validators.required],
    });
  }
  ngOnInit(): void {
    this.questionForm$.patchValue({
      category: this.type,
    });
  }

  submit() {
    if (!this.questionForm$.valid) return;
    if (this.selectedImagesFilesForUpload.length < 2) return;
    const category = this.type;

    const question: Questions = {
      id: '',
      levelID: this.questionForm$.get('levelID')?.value ?? '',
      gameID: this.gameID,
      question: '',
      image: null,
      answer: '',
      choices: [],
      type: category,
      createdAt: new Date(),
      updatedAt: new Date(),
      hint: '',
    };
    this.saveQuestion(question, this.selectedImagesFilesForUpload);
  }

  saveQuestion(question: Questions, file: File[]) {
    this.questionService
      .createMemoryQuestion(question, file)
      .then((data) => {
        this.toastr.success('Successfully added!');
      })
      .catch((err) => this.toastr.error(err['message']))
      .finally(() => this.activeModal.close());
  }
}
