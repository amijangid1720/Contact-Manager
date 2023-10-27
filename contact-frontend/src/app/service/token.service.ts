import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  constructor(private http: HttpClient) {}

  public getToken(email :string,password:string) :Observable<any>{
    // Send a GET request to your server's token retrieval endpoint
    // Adjust the URL as needed
    const params = {
      email: email,
      password: password
    };
    return this.http.post(`http://localhost:8082/api/v1/auth/authenticate`, { ... params });
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
}

