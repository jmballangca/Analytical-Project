import { Questions } from '../Questions';
import { Quiz } from './Quiz';

export interface QuizWithQuestions {
  quiz: Quiz;
  questions: Questions[];
}
