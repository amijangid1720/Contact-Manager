import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root',
})
export class AddServiceService {
  private infoUrl = `${environment.backendUrl}/api/v1/contacts/contactinfo`;
  constructor(private http: HttpClient) {}

  public addContact(contact: Object): Observable<any> {
    return this.http.post(`${environment.backendUrl}/api/v1/contacts/`, contact);
  }

  getContactById(contactId: number): Observable<any> {
    const url = `${this.infoUrl}/${contactId}`;
    return this.http.get(url);
  }

  updateContact(contact: Object, contactId: number): Observable<any> {
    const url = `${environment.backendUrl}/api/v1/contacts/update/${contactId}`; 

    return this.http.put(url, contact);
  }
}
