// token-refresh.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root',
})
export class TokenRefreshService {
  constructor(private http: HttpClient) {}

  refresh(): Observable<any> {
    const refreshToken = localStorage.getItem('refreshToken');
    console.log("calling refresh api");
    
    const url = `${environment.backendUrl}/api/v1/auth/refresh`;

    return this.http.post(url, { refreshToken: refreshToken });
  }
}
