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
    return this.http.get(`http://localhost:8082/api/v1/contacts/findAll`);
  }

  deleteContact(dub: string): Observable<any> {
    return this.http.delete(
      `http://localhost:8082/api/v1/contacts/delete/${dub}`
    );
  }
}
