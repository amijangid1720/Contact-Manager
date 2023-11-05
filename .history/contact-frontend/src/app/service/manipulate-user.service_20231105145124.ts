import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../environment';
@Injectable({
  providedIn: 'root',
})
export class ManipulateUserService implements OnInit {
  constructor(private http: HttpClient) {}
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }

  getContacts(): Observable<any> {
    return this.http.get(`${environment.backendUrl}/api/v1/contacts/findAll`);
  }

  deleteContact(id: string): Observable<any> {
    return this.http.delete(
      `${environment.backendUrl}/api/v1/contacts/delete/${id}`
    );
  }
}
