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

  getContacts(page: number, size: number,): Observable<any> {
    return this.http.get(`${environment.backendUrl}/api/v1/contacts/findAll?page=${page}&size=${size}`);
  }

  getUserById(userid: number): Observable<any> {
    const url = `${environment.backendUrl}/api/v1/contacts/userinfo/${userid}`;
    return this.http.get(url);
  }
  
  updateUser(user:object, userid:number):Observable<any> {
    const url = `${environment.backendUrl}/api/v1/contacts/updateUser/${userid}`;
     return this.http.put(url,user);
  }
 

updateDetailsFilled(userid:number):Observable<any>{
  const url = `${environment.backendUrl}/api/v1/contacts/updateDetailsFilled/${userid}`;
  return this.http.put(url,userid);
}
  
  deleteContact(id: string): Observable<any> {
    return this.http.delete(
      `${environment.backendUrl}/api/v1/contacts/delete/${id}`
    );
  }
}