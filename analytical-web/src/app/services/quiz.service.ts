import { Injectable } from '@angular/core';
import {
  Firestore,
  addDoc,
  collection,
  collectionData,
  doc,
  docData,
  getDocs,
  orderBy,
  query,
  setDoc,
  updateDoc,
  where,
  writeBatch,
} from '@angular/fire/firestore';

import { v4 as uuidv4 } from 'uuid';
import {
  Storage,
  deleteObject,
  getDownloadURL,
  ref,
  uploadBytes,
} from '@angular/fire/storage';
import { Observable } from 'rxjs';
import { Levels } from '../models/quiz/Levels';
import { LEVELS_COLLECTION } from './levels.service';
import { Quiz, quizConverter } from '../models/quiz/Quiz';
export const SUBMISSIONS_COLLECTION = 'submissions';
export const QUIZ_COLLECTION = 'quiz';
@Injectable({
  providedIn: 'root',
})
export class QuizService {
  constructor(private firestore: Firestore, private storage: Storage) {}
  publishGame(gameID: string) {
    return updateDoc(doc(this.firestore, QUIZ_COLLECTION, gameID), {
      visible: true,
    });
  }
  async createQuiz(quiz: Quiz): Promise<string | null> {
    try {
      const quizId = doc(collection(this.firestore, QUIZ_COLLECTION)).id;
      quiz.id = quizId;
      const batch = writeBatch(this.firestore);
      batch.set(
        doc(this.firestore, QUIZ_COLLECTION, quiz.id).withConverter(
          quizConverter
        ),
        quiz
      );

      for (let i = 0; i < quiz.levels; i++) {
        let level: Levels = {
          id: uuidv4(),
          name: 'Level ' + (i + 1),
          levelNumber: i + 1,
          questions: 10,
          points: 1,
          timer: 2,
        };
        const levelRef = doc(
          this.firestore,
          `${QUIZ_COLLECTION}/${quiz.id}/${LEVELS_COLLECTION}`,
          level.id
        );
        batch.set(levelRef, level);
      }

      await batch.commit();
      return quiz.id;
    } catch (error) {
      console.error('Error creating quiz: ', error);
      return null;
    }
  }
  async uploadFile(file: File) {
    try {
      const fireRef = ref(this.storage, `${QUIZ_COLLECTION}/${uuidv4()}`);
      await uploadBytes(fireRef, file);
      const downloadURL = await getDownloadURL(fireRef);
      return downloadURL;
    } catch (error) {
      console.error('Error uploading file:', error);
      throw error;
    }
  }

  getAllQuiz(): Observable<Quiz[]> {
    const q = query(
      collection(this.firestore, QUIZ_COLLECTION).withConverter(quizConverter),
      orderBy('createdAt', 'desc')
    );
    return collectionData(q) as Observable<Quiz[]>;
  }

  getQuizById(quizID: string): Observable<Quiz> {
    return docData(
      doc(this.firestore, QUIZ_COLLECTION, quizID).withConverter(quizConverter)
    ) as Observable<Quiz>;
  }
  async deleteQuiz(quizID: string, quizImage: string) {
    let batch = writeBatch(this.firestore);
    const submissionsRef = collection(this.firestore, SUBMISSIONS_COLLECTION);
    const submissionsQuery = query(
      submissionsRef,
      where('quizID', '==', quizID)
    );
    //delete all submissions
    const submissionsSnapshot = await getDocs(submissionsQuery);
    submissionsSnapshot.forEach((submissionDoc) => {
      batch.delete(submissionDoc.ref);
    });
    //delete all levels
    const levelsRef = collection(
      this.firestore,
      `${QUIZ_COLLECTION}/${quizID}/${LEVELS_COLLECTION}`
    );
    const levels = await getDocs(levelsRef);
    levels.forEach((level) => {
      batch.delete(level.ref);
    });

    //delete quiz
    batch.delete(doc(this.firestore, QUIZ_COLLECTION, quizID));
    //delete cover photo
    let storageRef = ref(this.storage, quizImage);
    //await deleteObject(storageRef);

    return batch.commit();
  }
}
