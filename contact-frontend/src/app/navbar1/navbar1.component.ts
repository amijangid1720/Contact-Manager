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
    private loginService: LoginService
  ) {}
  loggedin!: boolean;
  faUser = faUser;
  faPowerOff = faPowerOff;
  faArrowRightFromBracket = faArrowRightFromBracket;
  firstName: string = environment.username;

  ngOnInit() {
    this.loggedin = this.tokenservice.isLoggedin();
    console.log(this.loggedin);
    const yourAuthToken = localStorage.getItem('token');
    console.log(yourAuthToken);

    if (yourAuthToken) {
      console.log('hi');
      this.tokenservice.getUserName().subscribe({
        next: (res) => {
          console.log(res);
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

  openNav() {
    const sidenavElement = this.el.nativeElement.querySelector('#mySidenav');
    this.renderer.setStyle(sidenavElement, 'width', '250px');
  }

  closeNav() {
    const sidenavElement = this.el.nativeElement.querySelector('#mySidenav');
    this.renderer.setStyle(sidenavElement, 'width', '0');
  }

  signOut() {
    console.log('Logout button clicked good one');
    this.authService.signOut();
    this.loggedin = false;
    console.log(this.loggedin);

    this.router.navigate(['']);
    window.location.reload();
  }

  toggleNav() {
    const sidenavElement = this.el.nativeElement.querySelector('#mySidenav');
    const mainElement = this.el.nativeElement.querySelector('#main');

    const currentWidth = getComputedStyle(sidenavElement).width;

    if (currentWidth === '0px' || !currentWidth) {
      this.renderer.setStyle(sidenavElement, 'width', '250px');
      this.renderer.setStyle(mainElement, 'margin-left', '250px');
    } else {
      this.renderer.setStyle(sidenavElement, 'width', '0');
      this.renderer.setStyle(mainElement, 'margin-left', '0');
    }
  }
}
