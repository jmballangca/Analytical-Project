import { Questions } from './Questions';

export interface PaginatedQuestions {
  questions: Questions[];
  start: number;
  end: number;
  limit: number;
}
