import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from '../service/storage.service';
import { AuthService } from '../service/auth.service';
import { TokenService } from '../service/token.service';
import { faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent  implements OnInit{
  faArrowRightFromBracket = faArrowRightFromBracket;
  constructor(private router: Router,
    private storageService: StorageService,
    private authService: AuthService,
    private renderer: Renderer2,
    private el: ElementRef,
    private tokenService:TokenService
  ) {}
  firstName:string="";
  ngOnInit(): void {
    const yourAuthToken = localStorage.getItem('token');
    
    if (yourAuthToken !== null) { 
      this.tokenService.getUserName(yourAuthToken).subscribe({
        next: (res) => {
          console.log(res);
          this.firstName = res.firstName;
        },
        error: (err) => {
          console.log(err);
        },
      });
    } else {
      console.error("Token not found in localStorage."); 
    }
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
  signOut() {
    console.log('Logout button clicked');
    this.authService.signOut();
    this.router.navigate(['/']);
  }
}
