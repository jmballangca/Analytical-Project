import { QueryDocumentSnapshot } from '@angular/fire/firestore';
import { Levels } from '../quiz/Levels';
import { Questions } from '../Questions';
import { AnswerSheet } from './AnswerSheet';
import { QuizInfo } from './QuizInfo';
import { Administrators } from '../Administrator';
import { Performance } from './Performance';

export interface Submissions {
  id?: string;
  studentID?: string;
  quizInfo?: QuizInfo;
  answerSheet: AnswerSheet[];
  performance: Performance;
  createdAt: Date;
}

export const submissionsConverter = {
  toFirestore: (data: Submissions) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const submissions = snap.data() as Submissions;
    submissions.createdAt = (submissions.createdAt as any).toDate();
    return submissions;
  },
};
