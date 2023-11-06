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
    // Send a GET request to your server's token retrieval endpoint
    // Adjust the URL as needed
    const params = {
      email: email,
      password: password
    };
    return this.http.post(`${environment.apiUrl}auth/authenticate`, { ... params });
  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token'); // Assuming the token is stored in local storage

    // Check if the token is present and valid (you'll need to implement this validation logic)
    if (token) {
      // Implement your token validation logic here
      // You might want to check token expiration, signature, etc.
      return true; // Return true if the token is valid
    }

    return false; // No valid token
  }

  public getUserName(): Observable<any>{
    return this.http.get(`${environment.apiUrl}contacts/info`);

  }

  public fetchToken():any{
    return localStorage.getItem('token');

  }
}

