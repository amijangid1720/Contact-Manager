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

  public addContact(contact:Object,yourAuthToken: string):Observable<any>{

    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${yourAuthToken}` // Add your token here
    });

    const options = { headers: headers };

    return this.http.post(`http://localhost:8082/api/v1/contacts/`,contact ,options);
  }

  getContactById(contactId: number,yourAuthToken: string) : Observable<any>{

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${yourAuthToken}` // Add your token here
    });

    const options = { headers: headers };
    const url = `${this.baseUrl}/${contactId}`;
    return this.http.get(url,options);
  }


  updateContact(contact: Object, 
    authToken: string,contactId:number): Observable<any> {
    const url = `${this.updateUrl}/update/${contactId}`; // Adjust the URL to match your backend endpoint

    // Set the HTTP headers, including the authorization token
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });
    const options = { headers: headers };
    // Send a PUT request with the updated contact data
    return this.http.put(url, contact, options);
  }

  

}
