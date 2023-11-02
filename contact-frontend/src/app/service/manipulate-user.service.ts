import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { Observable, map } from 'rxjs';
// import { contacts } from '../model/contacts';

@Injectable({
  providedIn: 'root'
})
export class ManipulateUserService implements OnInit { 

  // allContacts: contacts[] = [];
  constructor(private http:HttpClient) { }
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }
  
  // getUserName(token:string, email:string): Observable<any>
  // // {
  //     const headers = new HttpHeaders({
  //       'Content-Type': 'application/json',
  //       'Authorization': `Bearer ${token}`,
  //     });
  //       return this.http.get(`http:localhost:8082/api/v1/get-name/${email}`, { headers });
  //       // `http:localhost:8082/api/v1/get-name/:email`
  // }


   getContacts(yourAuthToken:string):Observable<any> {
    const headers = new HttpHeaders({
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${yourAuthToken}`,
          });

        
    return this.http.get(`http://localhost:8082/api/v1/contacts/findAll`, {headers});

   }
 
}

 // const options = {headers:headers}
