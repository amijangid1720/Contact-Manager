import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AddServiceService {

  private baseUrl = 'http://localhost:8082/api/v1/contacts/contactinfo';
  private updateUrl='http://localhost:8082/api/v1/contacts';
  constructor(private http: HttpClient) {}

  public addContact(contact:Object):Observable<any>{

    return this.http.post(`http://localhost:8082/api/v1/contacts/`,contact);
  }

  getContactById(contactId: number) : Observable<any>{

    const url = `${this.baseUrl}/${contactId}`;
    return this.http.get(url);
  }


  updateContact(contact: Object, 
    contactId:number): Observable<any> {
    const url = `${this.updateUrl}/update/${contactId}`; // Adjust the URL to match your backend endpoint

   
    return this.http.put(url, contact);
  }
}
