import { Component, ElementRef, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { TokenService } from '../service/token.service';
import {
  faArrowRightFromBracket,
  faUser,
  faPowerOff,
} from '@fortawesome/free-solid-svg-icons';
import { environment } from '../environment';
import { LoginService } from '../service/login.service';
import { ManipulateUserService } from '../service/manipulate-user.service';

@Component({
  selector: 'app-navbar1',
  templateUrl: './navbar1.component.html',
  styleUrls: ['./navbar1.component.scss'],
})
export class Navbar1Component {
  constructor(
    private router: Router,
    private authService: AuthService,
    private renderer: Renderer2,
    private el: ElementRef,
    private tokenservice: TokenService,
    private loginService: LoginService,
    private manipulateuser:ManipulateUserService
  ) {}
  loggedin!: boolean;
  faUser = faUser;
  faPowerOff = faPowerOff;
  faArrowRightFromBracket = faArrowRightFromBracket;
  firstName: string = environment.username;
  contacts!:any;

  ngOnInit() {
    this.loggedin = this.tokenservice.isLoggedin();
    console.log(this.loggedin);
    const yourAuthToken = localStorage.getItem('token');
    console.log(yourAuthToken);

    if (yourAuthToken) {
      console.log('hi');
      this.tokenservice.getUserName().subscribe({
        next: (res) => {
          // console.log("res" + res);
          this.firstName = res.firstName;
          this.loginService.setLoggedInStatus(true);
        },
        error: (err) => {
          console.log(err);
        },
      });
    } else {
      console.error('Token not found in localStorage.');
      // Handle this case by logging out the user
      this.loginService.setLoggedInStatus(false);
      this.loggedin = false;
      this.loginService.clearToken();
      // this.signOut();
    }
  }

  // uploadContacts(){
  //   const userid = localStorage.getItem('user_id');
  //   console.log(userid);
    
  //   if (userid !== null) {
  //     const userId = parseInt(userid, 10);
  //   this.manipulateuser.getAllContact(userId).subscribe((data)=> {
  // this.contacts = data;
  // console.log("contacts",this.contacts);
  
  // this.manipulateuser.uploadContacts(this.contacts).subscribe(
  //   (response) => {
  //     console.log('Contacts uploaded successfully', response);

  //     // Here, you can trigger the additional logic to upload to Google Drive
  //   },
  //   (error) => {
  //     console.error('Error uploading contacts', error);
  //     // Handle error scenarios
  //   }
  // );
  //   });

  //  }}


  signOut() {
    console.log('Logout button clicked good one');
    this.authService.signOut();
    this.loggedin = false;
    console.log(this.loggedin);

    this.router.navigate(['']);
    window.location.reload();
  }

  
}
