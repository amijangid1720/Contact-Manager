import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private http: HttpClient,

  ) {}

  sendTokenToBackend(idToken: String): Observable<any> {
    console.log('hiii');

    const url = `${environment.backendUrl}/api/v1/auth/google`;
    return this.http.post(url, idToken);
  }

  signOut(): void {
    localStorage.clear();
  }
}
