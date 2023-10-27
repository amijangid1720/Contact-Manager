import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { TokenService } from '../service/token.service';
import {
  GoogleLoginProvider,
  SocialAuthService,
  SocialUser,
} from '@abacritt/angularx-social-login';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  user!: SocialUser;
  loggedIn!: boolean;

  loginData = {
    email: 'mkumari@gmail.com',
    password: '1234@',
  };
  constructor(
    private router: Router,
    private http: HttpClient,
    private tokenService: TokenService,
    private authService: SocialAuthService,
    private authBackendService: AuthService
  ) {}

  ngOnInit(): void {
    this.authService.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = user != null;
      console.log(user);
      console.log('token: ' + user.idToken);
      const googleIdToken = user.idToken;

    //   /// send id token to the backend
      this.authBackendService.sendTokenToBackend(googleIdToken).subscribe({
        next: (res) => {
          console.log(res);
          if(this.loggedIn){
            this.signOut();
          }
          localStorage.setItem('token', res.token);
          this.router.navigateByUrl('api/v1/dashboard');

        },
        error: (err) => {
          console.log(err);
        },
      });
     });
  }

  //for logi usinfg username-password
  login() {
    // request to generate token
    this.tokenService
      .getToken(this.loginData.email, this.loginData.password)
      .subscribe(
        (data: any) => {
          console.log('success');
          console.log(data);
          const token = data.token;

          // Store the token in a secure location (e.g., local storage)
          localStorage.setItem('token', token);

          // Redirect to the authenticated page (adjust the route as needed)
          this.router.navigateByUrl('api/v1/dashboard');
        },
        (error) => {
          console.log('Error');
        }
      );
  }

  ///for google auth

   signInWithGoogle(): void { 
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }
  // //   this.authService
  //     .signIn(GoogleLoginProvider.PROVIDER_ID)
  //     .then((user) => {
  //       this.user = user;
  //       this.loggedIn = true;

  //       const googleIdToken = user.idToken;

  //       //send id token to the backend
  //       this.authBackendService.sendTokenToBackend(googleIdToken).subscribe(
  //         response => {
  //           console.log('id send to backend', response);

  //         },
  //         error => {
  //           console.error("error sending id token")
  //         }
  //       )

  //     })
  //     .catch((error) => {
  //       console.error('Error during Google login', error);
  //     });
  // }

  signOut(): void {
    this.authService
      .signOut()
      .then(() => {
        console.log('sign out');
      })
      .catch((error) => {
        console.error('Error during logout', error);
      });
  }
}
