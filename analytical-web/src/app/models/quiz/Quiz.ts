import { QueryDocumentSnapshot } from '@angular/fire/firestore';
import { Questions } from '../Questions';

export interface Quiz {
  id: string;
  title: string;
  desc: string;
  cover_photo: string;
  subject: 'MATH' | 'ENGLISH';
  schoolLevel: 'SHS' | 'JHS';
  category: Questions['type'];
  levels: number;
  visible: boolean;
  createdAt: Date;
}

// export enum Categories {
//   REBUS_PUZZLE = 'REBUS_PUZZLE',
//   RIDDLES = 'RIDDLES',
//   WORD_PUZZLE = 'WORD PUZZLE',
//   MATH_LOGIC_PUZZLE = 'MATH LOGIC PUZZLE',
// }

// export enum Subjects {
//   MATH = 'MATH',
//   ENGLISH = 'ENGLISH',
// }
export const quizConverter = {
  toFirestore: (data: Quiz) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const quiz = snap.data() as Quiz;
    quiz.createdAt = (quiz.createdAt as any).toDate();
    return quiz;
  },
};
