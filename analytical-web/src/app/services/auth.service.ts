import { Injectable } from '@angular/core';
import {
  Auth,
  EmailAuthCredential,
  EmailAuthProvider,
  reauthenticateWithCredential,
  sendPasswordResetEmail,
  signOut,
  updatePassword,
} from '@angular/fire/auth';
import {
  Firestore,
  collection,
  collectionData,
  doc,
  getDoc,
  getDocs,
  limit,
  query,
  setDoc,
  updateDoc,
  where,
} from '@angular/fire/firestore';
import {
  createUserWithEmailAndPassword,
  signInWithEmailAndPassword,
} from '@firebase/auth';
import { BehaviorSubject, Observable, map } from 'rxjs';
import {
  Administrators,
  administratorConverter,
} from '../models/Administrator';
import { each } from 'chart.js/dist/helpers/helpers.core';
export const ADMINISTRATOR_COLLECTION = 'administrators';
@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private adminSubject: BehaviorSubject<Administrators | null> =
    new BehaviorSubject<Administrators | null>(null);
  public administrator$: Observable<Administrators | null> =
    this.adminSubject.asObservable();

  constructor(private auth: Auth, private firestore: Firestore) {}

  login(email: string, password: string) {
    return signInWithEmailAndPassword(this.auth, email, password);
  }

  async register(admin: Administrators, password: string) {
    const result = await createUserWithEmailAndPassword(
      this.auth,
      admin.email,
      password
    );

    admin.id = result.user?.uid;
    return setDoc(
      doc(this.firestore, ADMINISTRATOR_COLLECTION, admin.id).withConverter(
        administratorConverter
      ),
      admin
    );
  }

  logout() {
    return signOut(this.auth);
  }

  setAdmin(admin: Administrators | null) {
    this.adminSubject.next(admin);
  }

  async getAdminInfo(email: string): Promise<Administrators | null> {
    const q = query(
      collection(this.firestore, ADMINISTRATOR_COLLECTION).withConverter(
        administratorConverter
      ),
      where('email', '==', email),
      limit(1)
    );
    const querySnapshot = await getDocs(q);
    if (querySnapshot.empty) {
      return null;
    } else {
      return querySnapshot.docs[0].data();
    }
  }

  forgotPassword(email: string) {
    return sendPasswordResetEmail(this.auth, email);
  }

  async changePassword(oldPassword: string, newPassword: string) {
    try {
      const user = this.auth.currentUser;
      if (!user) {
        throw new Error(
          'User is not authenticated. Please log in and try again.'
        );
      }

      const credential = EmailAuthProvider.credential(
        user.email || '',
        oldPassword
      );
      const result = await reauthenticateWithCredential(user, credential);

      await updatePassword(result.user, newPassword);
      return 'Password changed successfully.';
    } catch (err: any) {
      if (err['code'] === 'auth/wrong-password') {
        throw new Error('The old password is incorrect. Please try again.');
      } else if (err['code'] === 'auth/user-mismatch') {
        throw new Error('User does not match the credentials provided.');
      } else if (err['code'] === 'auth/network-request-failed') {
        throw new Error(
          'Network error. Please check your connection and try again.'
        );
      } else {
        throw new Error(
          'An error occurred while changing the password. Please try again later.'
        );
      }
    }
  }
  listenToAdmin(email: string): Observable<Administrators | null> {
    const q = query(
      collection(this.firestore, ADMINISTRATOR_COLLECTION).withConverter(
        administratorConverter
      ),
      where('email', '==', email),
      limit(1)
    );
    return collectionData(q, { idField: 'id' }).pipe(
      map((admins) => {
        if (admins.length === 0) {
          return null;
        } else {
          return admins[0];
        }
      })
    );
  }
  editAdminName(id: string, name: string) {
    return updateDoc(doc(this.firestore, ADMINISTRATOR_COLLECTION, id), {
      name: name,
    });
  }
}
