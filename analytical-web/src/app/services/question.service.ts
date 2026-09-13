import { Injectable } from '@angular/core';
import {
  Firestore,
  arrayRemove,
  arrayUnion,
  collection,
  collectionData,
  deleteDoc,
  doc,
  docData,
  endAt,
  endBefore,
  getDoc,
  getDocs,
  increment,
  limit,
  orderBy,
  query,
  setDoc,
  startAfter,
  startAt,
  updateDoc,
  where,
  writeBatch,
} from '@angular/fire/firestore';
import { Questions, questionConverter } from '../models/Questions';
import { v4 as uuidv4 } from 'uuid';
import { QUIZ_COLLECTION, QuizService } from './quiz.service';
import {
  Observable,
  catchError,
  combineLatest,
  forkJoin,
  map,
  throwError,
} from 'rxjs';
import {
  ref,
  uploadBytes,
  getDownloadURL,
  Storage,
  deleteObject,
} from '@angular/fire/storage';

import { QuizWithQuestions } from '../models/quiz/QuizWithQuestions';
import { HttpClient } from '@angular/common/http';
import { Riddles } from '../models/Riddles';
import { ADMINISTRATOR_COLLECTION, AuthService } from './auth.service';
import {
  QUESTION_SETTINGS_COLLECTION,
  QUESTION_SETTINGS_DOCUMENT,
  QuestionSettings,
  getQuestionType,
  questionSettingsConverter,
} from '../models/quiz/QuestionSettings';
export const QUESTION_COLLECTION = 'questions';
export interface QuestionsWithSettings {
  questions: Questions[];
  settings?: QuestionSettings;
}
@Injectable({
  providedIn: 'root',
})
export class QuestionService {
  constructor(private firestore: Firestore, private storage: Storage) {}
  private async uploadFile(file: File): Promise<string> {
    const storageRef = ref(this.storage, `questions/${file.name}`);
    await uploadBytes(storageRef, file);
    return await getDownloadURL(storageRef);
  }

  // Create a new question with file upload
  async createQuestion(question: Questions, file?: File): Promise<void> {
    const questionRef = doc(
      collection(this.firestore, 'questions')
    ).withConverter(questionConverter);
    question.id = questionRef.id;
    if (file) {
      question.image = await this.uploadFile(file);
    }

    await setDoc(questionRef, question);
  }

  async createMemoryQuestion(question: Questions, files: File[]) {
    const questionRef = doc(
      collection(this.firestore, 'questions')
    ).withConverter(questionConverter);
    question.id = questionRef.id;
    const choices: string[] = await Promise.all(
      files.map(async (file) => {
        const result = await this.uploadFile(file);
        return result;
      })
    );
    question.choices = choices;
    await setDoc(questionRef, question);
  }

  getAllQuestionByGameID(gameID: string): Observable<Questions[]> {
    const q = query(
      collection(this.firestore, QUESTION_COLLECTION),
      where('gameID', '==', gameID),
      orderBy('createdAt', 'desc'),
      orderBy('updatedAt', 'desc')
    ).withConverter(questionConverter);
    return collectionData(q);
  }

  // Update an existing question with file upload
  async updateQuestion(question: Questions, file?: File): Promise<void> {
    const questionRef = doc(
      this.firestore,
      `questions/${question.id}`
    ).withConverter(questionConverter);
    if (file) {
      question.image = await this.uploadFile(file);
    }
    question.updatedAt = new Date();
    await updateDoc(questionRef, { ...question });
  }

  // Get questions by level ID
  getQuestionsByLevelID(levelID: string): Observable<Questions[]> {
    const questionsRef = collection(this.firestore, 'questions').withConverter(
      questionConverter
    );
    const q = query(questionsRef, where('levelID', '==', levelID));
    return collectionData(q).pipe(map((data) => data as Questions[]));
  }

  // Get all questions
  getAllQuestions(): Observable<Questions[]> {
    const questionsRef = collection(this.firestore, 'questions').withConverter(
      questionConverter
    );
    return collectionData(questionsRef).pipe(
      map((data) => data as Questions[])
    );
  }

  // Delete a question by ID
  async deleteQuestion(question: Questions): Promise<void> {
    const questionRef = doc(
      this.firestore,
      `questions/${question.id}`
    ).withConverter(questionConverter);
    await deleteDoc(questionRef);
  }
  updateGameID(gameID: string): void {
    const batch = writeBatch(this.firestore);
    const questionsRef = collection(this.firestore, 'questions');
    getDocs(questionsRef)
      .then((querySnapshot) => {
        querySnapshot.forEach((doc) => {
          const questionRef = doc.ref;
          batch.update(questionRef, { gameID: gameID });
        });
        batch
          .commit()
          .then(() => {
            console.log('All questions updated with the new levelID');
          })
          .catch((error) => {
            console.error('Error updating questions:', error);
          });
      })
      .catch((error) => {
        console.error('Error fetching questions:', error);
      });
  }

  async addChoices(questionID: string, files: File[]) {
    const arr: string[] = [];

    for (const file of files) {
      const result = await this.uploadFile(file);
      arr.push(result);
    }

    return updateDoc(
      doc(this.firestore, QUESTION_COLLECTION, questionID).withConverter(
        questionConverter
      ),
      {
        choices: arrayUnion(...arr),
      }
    );
  }
  deleteChoice(questionID: string, choice: string) {
    const choiceRef = ref(this.storage, choice);
    deleteObject(choiceRef);

    return updateDoc(
      doc(this.firestore, QUESTION_COLLECTION, questionID).withConverter(
        questionConverter
      ),
      {
        choices: arrayRemove(choice),
      }
    );
  }
}
