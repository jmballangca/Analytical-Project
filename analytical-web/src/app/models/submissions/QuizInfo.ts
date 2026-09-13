import { Levels } from '../quiz/Levels';
import { Category } from './Category';

export interface QuizInfo {
  id?: string;
  category?: Category;
  type?: 'MATH' | 'ENGLISH';
  name?: string;
  levels?: Levels;
}
