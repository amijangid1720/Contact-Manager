import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, catchError, filter, switchMap, take, throwError } from 'rxjs';
import { TokenService } from './token.service';
import { TokenRefreshService } from './token-refresh.service';

@Injectable({
  providedIn: 'root',
})
export class AuthInterceptorService implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(
    null
  );
  constructor(private tokenRefreshService: TokenRefreshService) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<any> {
    // Retrieve the authentication token from local storage
    const authToken = localStorage.getItem('token');

    // Clone the request and add the Authorization header if the token exists
    if (authToken) {
      const modifiedRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      return next.handle(modifiedRequest).pipe(
        catchError((error) => {
          console.log('The error occuring during request');
          console.log(error);
          localStorage.removeItem('token');
          if (error.status == 403 || error.status == 401) {
            
            return this.handleRefreshToken(request, next);
          }

          else
          return throwError(() => error);
        })
      );
    } else {
      
      return next.handle(request);
    }
  }
  handleRefreshToken(request: HttpRequest<any>, next: HttpHandler): any {
    console.log('entering refresh logic');
    return this.tokenRefreshService.refresh().pipe(
      switchMap((data: any) => {
        console.log('consoling data returned from refresh method');

        console.log(data);
        localStorage.setItem('token', data.token)
        const newToken = data.token;
        localStorage.setItem('refreshToken', data.refreshToken);
        console.log("REQUEST   ",request)
        return next.handle(

          request.clone({
            setHeaders: {
              Authorization: `Bearer ${newToken}`,
            },
          })
          
        );
      })
    );
  }
  // private addToken(request: HttpRequest<any>, token: string) {
  //   return request.clone({
  //     setHeaders: {
  //       Authorization: `Bearer ${token}`,
  //     },
  //   });
  // }

  // private handleRefreshToken(request: HttpRequest<any>, next: HttpHandler) {
  //   if (!this.isRefreshing) {
  //     this.isRefreshing = true;
  //     this.refreshTokenSubject.next(null);

  //     return this.tokenRefreshService.refresh().pipe(
  //       switchMap((Token: any) => {
  //         this.isRefreshing = false;
  //         this.refreshTokenSubject.next(Token.token);
  //         return next.handle(this.addToken(request, Token.token));
  //       })
  //     );
  //   } else {
  //     return this.refreshTokenSubject.pipe(
  //       filter((token) => token != null),
  //       take(1),
  //       switchMap((jwt) => {
  //         return next.handle(this.addToken(request, jwt));
  //       })
  //     );
  //   }
  // }
}
