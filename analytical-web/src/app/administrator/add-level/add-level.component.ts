import { Component, Input, OnInit, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Levels } from '../../models/quiz/Levels';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { getNumberInString } from '../../utils/Constants';
import { ToastrService } from 'ngx-toastr';
import { LevelsService } from '../../services/levels.service';

@Component({
  selector: 'app-add-level',
  templateUrl: './add-level.component.html',
  styleUrl: './add-level.component.css',
})
export class AddLevelComponent implements OnInit {
  activeModal = inject(NgbActiveModal);
  @Input() quizID: string = '';
  @Input() count: number = 0;

  levelForm$: FormGroup;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private levelService: LevelsService
  ) {
    this.levelForm$ = fb.group({
      name: new FormControl(null, [Validators.required]),
      questions: new FormControl(null, [Validators.required]),
      points: new FormControl(10, [Validators.required]),
      timer: new FormControl(5, [Validators.required]),
    });
  }
  ngOnInit(): void {
    this.levelForm$.patchValue({
      name: 'Level ' + (this.count + 1),
      questions: 5,

      timer: 5,
    });
  }

  onSubmit() {
    if (this.levelForm$.invalid) {
      this.toastr.error('Invalid level form');
      return;
    }
    let values = this.levelForm$.value;

    let newLevel: Levels = {
      id: '',
      name: values.name,
      questions: values.questions,
      points: +values.points,
      timer: values.timer,
      levelNumber: this.count + 1,
    };
    this.saveLevel(this.quizID, newLevel);
  }
  saveLevel(quizID: string, level: Levels) {
    this.levelService
      .addLevels(quizID, level)
      .then((data) => this.toastr.success('Successfully Created'))
      .catch((err) => this.toastr.error(err['message'].toString()))
      .finally(() => this.activeModal.close());
  }
}
