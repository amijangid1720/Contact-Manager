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

  updateUser(user:Object, userId:number): Observable<any>{
const url = `${environment.backendUrl}/api/v1/contacts/updateuser/${userId}`;

return this.http.put(url,user);
  }

  checkContact(email:String, phoneno:Number): Observable<any>{
         const url = `${environment.backendUrl}/api/v1/contacts/checkDuplicateContact`;
    return this.http.post(url, {email, phoneno})
        }


        toggleFavorite(contactId: number, isFavorite: boolean): Observable<void> {
          const url = `${environment.backendUrl}/api/v1/contacts/${contactId}/favorite`;
          return this.http.patch<void>(url, isFavorite);  // Send only the boolean value
        }
        
}