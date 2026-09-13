import { Injectable } from '@angular/core';
import {
  addDoc,
  collection,
  collectionData,
  deleteDoc,
  doc,
  Firestore,
  QueryDocumentSnapshot,
  setDoc,
} from '@angular/fire/firestore';
import { v4 as uuidv4 } from 'uuid';
import { Observable } from 'rxjs';
import { getNumberInString } from '../utils/Constants';
//model
export interface Todo {
  id: string;
  name: string;
  isDone: boolean;
  createdAt: Date;
}

export const todoCoverter = {
  toFirestore: (data: Todo) => data,
  fromFirestore: (snap: QueryDocumentSnapshot) => {
    const todo = snap.data() as Todo;
    todo.createdAt = (todo.createdAt as any).toDate();
    return todo;
  },
};

//service
@Injectable({
  providedIn: 'root',
})
export class TodoService {
  TABLE = 'todo';
  constructor(private firestore: Firestore) {}

  async sample() {
    try {
      const docRef = await addDoc(collection(this.firestore, 'users'), {
        first: 'Ada',
        last: 'Lovelace',
        born: 1815,
      });
      console.log('Document written with ID: ', docRef.id);
    } catch (e) {
      console.error('Error adding document: ', e);
    }
  }
  //create todo
  addTodo(todo: Todo) {
    todo.id = uuidv4();

    return setDoc(doc(this.firestore, this.TABLE, todo.id), todo);
  }

  //get all todo data
  viewAll(): Observable<Todo[]> {
    return collectionData(
      collection(this.firestore, this.TABLE).withConverter(todoCoverter)
    );
  }
  //delete todo
  delete(id: string) {
    return deleteDoc(doc(this.firestore, this.TABLE, id));
  }
}
