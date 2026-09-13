import { Component, Input, inject } from '@angular/core';
import {
  FormGroup,
  FormBuilder,
  FormControl,
  Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { Levels } from '../../models/quiz/Levels';
import { LevelsService } from '../../services/levels.service';

@Component({
  selector: 'app-update-level',
  templateUrl: './update-level.component.html',
  styleUrl: './update-level.component.css',
})
export class UpdateLevelComponent {
  activeModal = inject(NgbActiveModal);
  @Input() quizID: string = '';
  @Input() level!: Levels;

  levelForm$: FormGroup;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private levelService: LevelsService
  ) {
    this.levelForm$ = fb.group({
      name: new FormControl([null, Validators.required]),
      questions: new FormControl([null, Validators.required]),
      points: new FormControl([1, Validators.required]),
      timer: new FormControl([null, Validators.required]),
    });
  }
  ngOnInit(): void {
    this.levelForm$.patchValue({
      name: this.level?.name,
      questions: this.level?.questions,
      points: this.level?.points ?? 1,
      timer: this.level?.timer,
    });
  }

  onSubmit() {
    if (this.levelForm$.invalid) {
      this.toastr.error('Invalid level form');
      return;
    }
    let values = this.levelForm$.value;
    let newLevel: Levels = {
      id: this.level.id,
      name: values.name,
      questions: values.questions,
      points: values.points,
      timer: values.timer,
      levelNumber: this.level.levelNumber,
    };
    this.saveUpdate(this.quizID, newLevel);
  }
  saveUpdate(quizID: string, level: Levels) {
    this.levelService
      .updateLevel(quizID, level)
      .then((data) => this.toastr.success('Successfully Updated'))
      .catch((err) => this.toastr.error(err['message'].toString()))
      .finally(() => this.activeModal.close());
  }
}
