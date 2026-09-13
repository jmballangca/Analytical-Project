import { QueryDocumentSnapshot } from '@angular/fire/firestore';
import { SchoolLevel } from './GradeLevel';

export interface Students {
  id: string;
  email: string;
  fname: string;
  mname: string;
  lname: string;
  profile: string;
  schoolLevel: SchoolLevel;
}
export const studentConverter = {
  toFirestore: (data: Students) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const student = snap.data() as Students;
    return student;
  },
};
