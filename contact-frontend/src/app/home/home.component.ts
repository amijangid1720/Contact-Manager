import { Component } from '@angular/core';
import { TokenService } from '../service/token.service';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';
import {
  faAddressBook,
  faUserPlus,
  faUser,
  faLock,
  faHeart,
  faTableCellsLarge,
  faPeopleGroup,
  faHouseChimneyWindow,
  faBriefcase
} from '@fortawesome/free-solid-svg-icons';
import { LoginService } from '../service/login.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent {
  faAddressBook = faAddressBook;
  faUserPlus = faUserPlus;
  faLock = faLock;
  faUser = faUser;
  faHeart = faHeart;
  faTableCellsLarge= faTableCellsLarge;
  faHouseChimneyWindow=faHouseChimneyWindow;
  faPeopleGroup=faPeopleGroup;
  faBriefcase=faBriefcase;
  
  
  loggedin!: boolean;
  constructor(
    private tokenservice: TokenService,
    private authService: AuthService,
    private router: Router,
    private loginService: LoginService
  ) {}

  ngOnInit() {}

  isLoggedout(): boolean {
    if (this.tokenservice.fetchToken()) return false;
    else return true;
  }
  signOut() {
    //console.log('Logout button clicked');
    this.authService.signOut();
    this.loggedin = false;
    //console.log(this.loggedin);

    this.router.navigate(['']);
    window.location.reload();
  }
}
