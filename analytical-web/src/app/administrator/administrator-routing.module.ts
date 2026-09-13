import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ViewQuizComponent } from './games/view-quiz/view-quiz.component';
import { MainComponent } from './main/main.component';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DashboardComponent } from './dashboard/dashboard.component';
import { GamesComponent } from './games/games.component';
import { SubmissionsComponent } from './submissions/submissions.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';

import { QuestionsComponent } from './questions/questions.component';
import { ProfileComponent } from '../auth/profile/profile.component';
import { StudentsComponent } from './students/students.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
  },
  {
    path: 'students',
    component: StudentsComponent,
  },
  {
    path: 'leaderboard',
    component: LeaderboardComponent,
  },
  {
    path: 'games',
    component: GamesComponent,
  },
  {
    path: 'questions',
    component: QuestionsComponent,
  },
  {
    path: 'games/view/:id',
    component: ViewQuizComponent,
  },
  {
    path: 'submissions',
    component: SubmissionsComponent,
  },
  {
    path: 'profile',
    component: ProfileComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule, CommonModule, ReactiveFormsModule, FormsModule],
})
export class AdministratorRoutingModule {}
