import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from '../environment';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private loggedInSubject: BehaviorSubject<boolean> =
    new BehaviorSubject<boolean>(this.loginService.isAuthenticated());
  constructor(private http: HttpClient, private loginService: LoginService) {}

  sendTokenToBackend(idToken: String): Observable<any> {
    //console.log('hiii');

    const url = `${environment.backendUrl}/api/v1/auth/google`;
    return this.http.post(url, idToken);
  }
  setLoggedInStatus(loggedIn: boolean) {
    this.loggedInSubject.next(loggedIn);
  }

  getLoggedInStatus(): Observable<boolean> {
    return this.loggedInSubject.asObservable();
  }

  signOut(): void {
    localStorage.clear();
    // this.loginService.clearToken();
  }
}
