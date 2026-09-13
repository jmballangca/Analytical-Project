import { Questions } from '../Questions';

export interface AnswerSheet {
  questions?: Questions;
  answer?: string;
  correct: boolean;
  points: number;
}
