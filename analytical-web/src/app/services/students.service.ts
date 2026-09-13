import { Injectable } from '@angular/core';
import {
  collection,
  collectionData,
  deleteDoc,
  doc,
  Firestore,
  getDocs,
  query,
  setDoc,
  updateDoc,
  writeBatch,
} from '@angular/fire/firestore';
import { studentConverter, Students } from '../models/students/Student';
import { Observable } from 'rxjs';
import {
  deleteObject,
  getDownloadURL,
  ref,
  Storage,
  uploadBytes,
} from '@angular/fire/storage';

import { v4 as uuidv4 } from 'uuid';
import { SUBMISSIONS_COLLECTION } from './quiz.service';
import { EncriptionService } from './encription.service';
export const STUDENT_COLLECTION = 'students';
@Injectable({
  providedIn: 'root',
})
export class StudentsService {
  constructor(
    private firestore: Firestore,
    private storage: Storage,
    private encriptionService: EncriptionService
  ) {}

  async uploadFile(file: File) {
    try {
      const fireRef = ref(this.storage, `${STUDENT_COLLECTION}/${uuidv4()}`);
      await uploadBytes(fireRef, file);
      const downloadURL = await getDownloadURL(fireRef);
      return downloadURL;
    } catch (error) {
      console.error('Error uploading file:', error);
      throw error;
    }
  }

  async updateStudent(student: Students, file: File | null = null) {
    if (file) {
      const downloadURL = await this.uploadFile(file);
      student.profile = downloadURL;
    }
    return updateDoc(
      doc(this.firestore, STUDENT_COLLECTION, student.id).withConverter(
        studentConverter
      ),
      student
    );
  }

  getAllStudent(): Observable<Students[]> {
    const q = query(
      collection(this.firestore, STUDENT_COLLECTION).withConverter(
        studentConverter
      )
    );
    return collectionData(q);
  }
}
