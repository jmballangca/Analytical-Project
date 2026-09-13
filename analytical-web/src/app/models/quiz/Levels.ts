import { QueryDocumentSnapshot } from '@angular/fire/firestore';
import { Questions } from '../Questions';

export interface Levels {
  id: string;
  name: string;
  levelNumber: number;
  questions: number;
  points: number;
  timer: number;
}

export const quizConverter = {
  toFirestore: (data: Levels) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const quiz = snap.data() as Levels;

    return quiz;
  },
};
