import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor{

  constructor(private tokenService:TokenService) { }
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Retrieve the authentication token from local storage
    const authToken = localStorage.getItem('token');

    // Clone the request and add the Authorization header if the token exists
    if (authToken) {
      const modifiedRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      return next.handle(modifiedRequest);
    } else {
      // Handle the case where the authentication token is not available
      return next.handle(request);
    }
  }
  }

