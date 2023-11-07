import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  constructor(private http: HttpClient) {}

  public isLoggedin():boolean{
    if(localStorage.getItem('token'))
      return true;
    else
        return false;
  }

  public getToken(email :string,password:string) :Observable<any>{
  
    const params = {
      email: email,
      password: password
    };
    return this.http.post(`${environment.backendUrl}/api/v1/auth/authenticate`, { ... params });
  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');

   
    if (token) {
     
      return true; 
    }

    return false; 
  }

  public getUserName(): Observable<any>{
    return this.http.get(`${environment.backendUrl}/api/v1/contacts/info`);

  }


}
