import { NgModule } from '@angular/core';
import { AsyncPipe, CommonModule } from '@angular/common';
import { MainComponent } from './main/main.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { GamesComponent } from './games/games.component';
import { AdministratorRoutingModule } from './administrator-routing.module';

import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { NgChartsModule } from 'ng2-charts';
import { ViewQuizComponent } from './games/view-quiz/view-quiz.component';

import { QuestionsComponent } from './questions/questions.component';
import {
  NgbDropdown,
  NgbDropdownModule,
  NgbNavModule,
  NgbPagination,
} from '@ng-bootstrap/ng-bootstrap';

import { AddLevelComponent } from './add-level/add-level.component';
import { UpdateLevelComponent } from './update-level/update-level.component';

import { LevelsTableComponent } from './levels-table/levels-table.component';

import { ProfileComponent } from '../auth/profile/profile.component';
import { SubmissionsComponent } from './submissions/submissions.component';
import { StudentsComponent } from './students/students.component';
import { ViewQuestionComponent } from './questions/view-question/view-question.component';
import { SubmissionsTableComponent } from './games/submissions-table/submissions-table.component';
import { EditGameComponent } from './games/edit-game/edit-game.component';
import { DeleteConfirmationComponent } from './games/delete-confirmation/delete-confirmation.component';
import { QuestionsTableComponent } from './games/questions-table/questions-table.component';
import { CreateMemoryQuestionComponent } from './questions/create-memory-question/create-memory-question.component';
import { ReactiveFormsModule } from '@angular/forms';
import { EditQuestionComponent } from './questions/edit-question/edit-question.component';
import { EditMemoryQuestionComponent } from './questions/edit-memory-question/edit-memory-question.component';

@NgModule({
  declarations: [
    MainComponent,
    GamesComponent,
    ViewQuizComponent,
    DashboardComponent,
    ProfileComponent,
    SubmissionsComponent,
    LeaderboardComponent,
    QuestionsComponent,
    LevelsTableComponent,
    AddLevelComponent,
    UpdateLevelComponent,
    StudentsComponent,
    ViewQuestionComponent,
    SubmissionsTableComponent,
    EditGameComponent,
    DeleteConfirmationComponent,
    QuestionsTableComponent,
    CreateMemoryQuestionComponent,
  ],
  imports: [
    CommonModule,
    AdministratorRoutingModule,
    NgChartsModule,
    NgbDropdownModule,
    NgbPagination,
    ReactiveFormsModule,
    NgbNavModule,
    AsyncPipe,
  ],
})
export class AdministratorModule {}
