import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ManipulateUserService {

  constructor(private http:HttpClient) { }

  getUserName(token:string, email:string): Observable<any>
  {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      });
        return this.http.get(`http:localhost:8082/api/v1/get-name/${email}`, { headers });
        // `http:localhost:8082/api/v1/get-name/:email`
  }
}
