import { Submissions } from '../submissions/Submissions';
import { Students } from './Student';

export interface StudentWithSubmissions {
  student: Students;
  submissions: Submissions[];
  totalMatches: number;
  points: number;
}
