import { QueryDocumentSnapshot } from '@angular/fire/firestore';

export interface Questions {
  id: string;
  gameID: string;
  levelID: string;
  question: string;
  image: string | null;
  hint: string;
  answer: string;
  choices: string[];
  type: 'QUIZ_GAME' | 'PUZZLE_GAME' | 'MEMORY_GAME' | 'MATH_GAME';
  createdAt: Date;
  updatedAt: Date;
}

export const questionConverter = {
  toFirestore: (data: Questions) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const question = snap.data() as Questions;
    question.createdAt = (question.createdAt as any).toDate();
    question.updatedAt = (question.updatedAt as any).toDate();
    return question;
  },
};
