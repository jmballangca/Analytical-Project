import { Injectable } from '@angular/core';
import {
  FieldValue,
  Firestore,
  collection,
  collectionData,
  doc,
  getDocs,
  increment,
  orderBy,
  query,
  setDoc,
  updateDoc,
  where,
  writeBatch,
} from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { Questions, questionConverter } from '../models/Questions';
import { QUESTION_COLLECTION } from './question.service';
import { QUIZ_COLLECTION, SUBMISSIONS_COLLECTION } from './quiz.service';
import { Levels } from '../models/quiz/Levels';
import { v4 as uuidv4 } from 'uuid';
export const LEVELS_COLLECTION = 'levels';

@Injectable({
  providedIn: 'root',
})
export class LevelsService {
  constructor(private firestore: Firestore) {}

  getLevels(quizID: string): Observable<Levels[]> {
    let q = query(
      collection(
        this.firestore,
        `${QUIZ_COLLECTION}/${quizID}/${LEVELS_COLLECTION}`
      ),
      orderBy('levelNumber', 'asc')
    );
    return collectionData(q) as Observable<Levels[]>;
  }

  addLevels(quizID: string, level: Levels) {
    level.id = uuidv4();
    let batch = writeBatch(this.firestore);
    batch.update(doc(this.firestore, QUIZ_COLLECTION, quizID), {
      levels: increment(1),
    });
    batch.set(
      doc(
        this.firestore,
        `${QUIZ_COLLECTION}/${quizID}/${LEVELS_COLLECTION}`,
        level.id
      ),
      level
    );
    return batch.commit();
  }
  async deleteLevel(quizID: string, levelID: string) {
    const batch = writeBatch(this.firestore);
    const submissionsRef = collection(this.firestore, SUBMISSIONS_COLLECTION);
    const submissionsQuery = query(
      submissionsRef,
      where('levelID', '==', levelID)
    );
    const submissionsSnapshot = await getDocs(submissionsQuery);
    submissionsSnapshot.forEach((submissionDoc) => {
      batch.delete(submissionDoc.ref);
    });
    const levelRef = doc(
      this.firestore,
      `${QUIZ_COLLECTION}/${quizID}/${LEVELS_COLLECTION}/${levelID}`
    );
    batch.update(doc(this.firestore, QUIZ_COLLECTION, quizID), {
      levels: increment(-1),
    });
    batch.delete(levelRef);

    await batch.commit();
  }

  updateLevel(quizID: string, level: Levels) {
    const levelRef = doc(
      this.firestore,
      `${QUIZ_COLLECTION}/${quizID}/${LEVELS_COLLECTION}/${level.id}`
    );
    return setDoc(levelRef, level);
  }
}
