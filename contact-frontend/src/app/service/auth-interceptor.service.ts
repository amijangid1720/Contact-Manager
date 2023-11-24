import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  BehaviorSubject,
  Observable,
  catchError,
  filter,
  switchMap,
  take,
  tap,
  throwError,
} from 'rxjs';
import { TokenRefreshService } from './token-refresh.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthInterceptorService implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(
    null
  );
  constructor(
    private tokenRefreshService: TokenRefreshService,
    private router: Router
  ) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<any> {
    const authToken = localStorage.getItem('token');

    if (authToken) {
      const modifiedRequest = request.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`,
        },
      });
      return next.handle(modifiedRequest).pipe(
        tap((response) => {}),
        catchError((error) => {
          // console.log('3)The error occuring during request');
          // console.log('4)The URL giving above error is',error.url);
          
          console.log(error);
          localStorage.removeItem('token');
          if (error.status == 403 || error.status == 401) {
            this.router.navigateByUrl('');
            //this.router.navigateByUrl(error.url);
            return this.handleRefreshToken(request, next);
          } else return throwError(() => error);
        })
      );
    } else {
      return next.handle(request);
    }
  }
  handleRefreshToken(request: HttpRequest<any>, next: HttpHandler): any {
    return this.tokenRefreshService.refresh().pipe(
      switchMap((data: any) => {
        localStorage.setItem('token', data.token);
        const newToken = data.token;
        localStorage.setItem('refreshToken', data.refreshToken);

        return next
          .handle(
            request.clone({
              setHeaders: {
                Authorization: `Bearer ${newToken}`,
              },
            })
          )
          .pipe(
            tap((response) => {
              console.log('Response of Refresh Handle');
            }),
            catchError((error) => {
              console.log('Error of Refresh Handle');
              return throwError(() => error);
            })
          );
      })
    );
  }
}
