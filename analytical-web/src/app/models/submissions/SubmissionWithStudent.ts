import { Students } from '../students/Student';
import { Submissions } from './Submissions';

export interface SubmissionWithStudent {
  students: Students | null;
  submission: Submissions;
}
