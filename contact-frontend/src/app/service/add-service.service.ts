import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AddServiceService {

  constructor(private http: HttpClient) {}

  public addContact(contact:Object,yourAuthToken: string):Observable<any>{

    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${yourAuthToken}` // Add your token here
    });

    const options = { headers: headers };

    return this.http.post(`http://localhost:8082/api/v1/contacts/`,contact ,options);
  }

}
