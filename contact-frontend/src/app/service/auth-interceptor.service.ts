import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap, take } from 'rxjs/operators';
import { TokenService } from './token.service';
import { TokenRefreshService } from './token-refresh.service';

@Injectable({
  providedIn: 'root',
})
export class AuthInterceptorService implements HttpInterceptor {
  constructor(
    private tokenService: TokenService,
    private tokenRefreshService: TokenRefreshService
  ) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const authToken = this.tokenService.fetchToken();

    if (authToken) {
      request = this.addToken(request, authToken);
    }

    return next.handle(request).pipe(
      catchError((error) => {
        if (error.status === 401 || error.status == 403) {
          // Token expired, attempt to refresh
          console.log('Token Expired');

          return this.handle401Error(request, next);
        }

        return throwError(error);
      })
    );
  }

  private addToken(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  private handle401Error(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<any> {
    return this.tokenRefreshService.refresh().pipe(
      take(1),
      switchMap(
        (response) => {
          const newToken = response.token;

          // Update the token in local storage
          localStorage.setItem('token', newToken);

          // Retry the request with the new token
          request = this.addToken(request, newToken);
          return next.handle(request);
        },
        (error) => {
          // Handle refresh token error
          console.error('Error refreshing token', error);
          // You can propagate the error to the original observable
        }
      )
    );
  }
}
