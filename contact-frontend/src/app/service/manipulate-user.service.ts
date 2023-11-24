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

  getFavorite(userid:number):Observable<any>{
    const url = `${environment.backendUrl}/api/v1/contacts/favorite/${userid}`;
    return this.http.get(url);

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


removeFromFavorites(contactId: number):Observable<any>{
 const url = `${environment.backendUrl}/api/v1/contacts/${contactId}/unfavorite`;
 return this.http.patch(url,null);
}


uploadProfilePicture(formData: FormData,userid:number): Observable<any> {
  const uploadUrl = `${environment.backendUrl}/api/v1/contacts/upload-profile-picture/${userid}`;

  return this.http.post(uploadUrl, formData);
}
  
  deleteContact(id: string): Observable<any> {
    return this.http.delete(
      `${environment.backendUrl}/api/v1/contacts/delete/${id}`
    );
  }
}