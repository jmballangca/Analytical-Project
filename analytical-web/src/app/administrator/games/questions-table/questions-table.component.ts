import { Component, inject, Input, OnInit } from '@angular/core';
import { QuestionService } from '../../../services/question.service';
import { QuestionsWithLevels } from '../view-quiz/view-quiz.component';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { CreateQuestionComponent } from '../../questions/create-question/create-question.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Questions } from '../../../models/Questions';
import { Levels } from '../../../models/quiz/Levels';
import { DeleteConfirmationComponent } from '../delete-confirmation/delete-confirmation.component';
import { ToastrService } from 'ngx-toastr';
import { FormControl } from '@angular/forms';
import { Quiz } from '../../../models/quiz/Quiz';
import { CreateMemoryQuestionComponent } from '../../questions/create-memory-question/create-memory-question.component';
import { EditGameComponent } from '../edit-game/edit-game.component';
import { EditMemoryQuestionComponent } from '../../questions/edit-memory-question/edit-memory-question.component';
import { EditQuestionComponent } from '../../questions/edit-question/edit-question.component';

@Component({
  selector: 'app-questions-table',
  templateUrl: './questions-table.component.html',
  styleUrl: './questions-table.component.css',
})
export class QuestionsTableComponent implements OnInit {
  @Input() game!: Quiz;
  @Input() levels: Levels[] = [];
  questions: Questions[] = []; // assuming your Submission model
  currentPage = 1;
  pageSize = 50;
  paginatedQuestions: Questions[] = [];
  searchControl: FormControl = new FormControl('');
  constructor(
    private questionService: QuestionService,
    private toastr: ToastrService,
    private modalService: NgbModal
  ) {}

  selectedQuestion: Questions | null = null;
  selectQuestion(question: Questions) {
    this.selectedQuestion = question;
    console.log(question.id);
  }
  ngOnInit(): void {
    this.questionService
      .getAllQuestionByGameID(this.game.id)
      .subscribe((data) => {
        this.questions = data;
        this.updatePaginatedQuestion();
      });
    this.searchControl.valueChanges.subscribe(() => {
      this.updatePaginatedQuestion();
    });
  }

  updatePaginatedQuestion(): void {
    const searchTerm = this.searchControl.value.toLowerCase();

    // Filter questions based on the search term
    const filteredSubmissions = this.questions.filter((question) => {
      return (
        question.question?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        question.levelID?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    });

    // Sort filtered questions by createdAt in ascending order
    filteredSubmissions.sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
      return dateA - dateB;
    });
    const startItem = (this.currentPage - 1) * this.pageSize;
    const endItem = startItem + this.pageSize;
    this.paginatedQuestions = filteredSubmissions.slice(startItem, endItem);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.updatePaginatedQuestion();
  }

  deleteQuestion(question: Questions) {
    const modal = this.modalService.open(DeleteConfirmationComponent);
    modal.componentInstance.message = `Are you sure you want to delete
    ${question.question} ?`;
    modal.result.then((data) => {
      if (data === 'YES') {
        this.questionService
          .deleteQuestion(question)
          .then((data) => this.toastr.success('successfully deleted'))
          .catch((err) => this.toastr.error(err['message']));
      }
    });
  }

  editQuestion(question: Questions, levels: Levels[]) {
    if (this.game.category === 'MEMORY_GAME') {
      const modal = this.modalService.open(EditMemoryQuestionComponent, {
        size: 'md',
      });
      modal.componentInstance.question = question;
      modal.componentInstance.levels = levels;
      modal.componentInstance.type = this.game.category;
      modal.componentInstance.gameID = this.game.id;
    } else {
      const modal = this.modalService.open(EditQuestionComponent, {
        size: 'xl',
      });
      modal.componentInstance.question = question;
      modal.componentInstance.levels = levels;
      modal.componentInstance.type = this.game.category;
      modal.componentInstance.gameID = this.game.id;
    }
  }
  createQuestion(levels: Levels[]) {
    if (this.game.category === 'MEMORY_GAME') {
      const modal = this.modalService.open(CreateMemoryQuestionComponent, {
        size: 'md',
      });
      modal.componentInstance.levels = levels;
      modal.componentInstance.type = this.game.category;
      modal.componentInstance.gameID = this.game.id;
    } else {
      const modal = this.modalService.open(CreateQuestionComponent, {
        size: 'xl',
      });
      modal.componentInstance.levels = levels;
      modal.componentInstance.type = this.game.category;
      modal.componentInstance.gameID = this.game.id;
    }
  }

  handleImageSelection(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input && input.files) {
      const files = Array.from(input.files);

      if (this.selectQuestion == null) return;
      this.questionService
        .addChoices(this.selectedQuestion?.id!!, files)
        .then(() => this.toastr.success('successfully added'))
        .catch((err) => this.toastr.error(err['message']));
    }
  }
  deleteChoice(questionID: string, choice: string) {
    this.questionService
      .deleteChoice(questionID, choice)
      .then(() => this.toastr.success('successfully deleted'))
      .catch((err) => this.toastr.error(err['message']));
  }
}
