import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private backendUrl = 'http://localhost:8082';

  constructor(private http: HttpClient,
    private storageService: StorageService) {}

  sendTokenToBackend(idToken: String): Observable<any> {
    console.log('hiii');

    const url = `${this.backendUrl}/api/v1/auth/google`;
    return this.http.post(url, idToken);
  }


  signOut(): void {
  localStorage.clear();
  }
}
