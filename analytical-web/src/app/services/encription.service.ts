import { Injectable } from '@angular/core';

import * as CryptoJS from 'crypto-js';
import { environment } from '../../environments/environment.development';
@Injectable({
  providedIn: 'root',
})
export class EncriptionService {
  private secretKey = environment.appId;
  constructor() {}

  encrypt(password: string): string {
    return CryptoJS.AES.encrypt(password, this.secretKey).toString();
  }

  decrypt(encryptedPassword: string): string {
    const bytes = CryptoJS.AES.decrypt(encryptedPassword, this.secretKey);
    return bytes.toString(CryptoJS.enc.Utf8);
  }
}
