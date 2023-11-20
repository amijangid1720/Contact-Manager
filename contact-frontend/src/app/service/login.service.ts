import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private loggedInSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(this.isAuthenticated());
  constructor() { }

  setLoggedInStatus(loggedIn: boolean) {
    this.loggedInSubject.next(loggedIn);
  }

  getLoggedInStatus(): Observable<boolean> {
    return this.loggedInSubject.asObservable();
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  clearToken() {
    localStorage.removeItem('token');
    localStorage.clear();
    this.setLoggedInStatus(false);
  }
}
