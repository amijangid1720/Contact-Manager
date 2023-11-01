import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from '../service/storage.service';
import { AuthService } from '../service/auth.service';
import { TokenService } from '../service/token.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  constructor(
    private router: Router,
    private storageService: StorageService,
    private authService: AuthService,
    private renderer: Renderer2,
    private el: ElementRef,
    private tokenService:TokenService
  ) {}
  firstName:string="";
  ngOnInit(): void {
    // const storedToken = localStorage.getItem('token');
    // if(storedToken){
    //   this.authService.getUserInfo.getItem.subscribe({
    //     next:(userInfo: any) => {
    //       this.userName = userInfo;
    //     },

    //   })
    // }
    const yourAuthToken = localStorage.getItem('token');
    // this.tokenService.getUserName(yourAuthToken).subscribe((data: any) => {
    //   console.log(data);
    //   this.firstName = data.firstName;
    // });
    if (yourAuthToken !== null) { // Check if it's not null
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
      console.error("Token not found in localStorage."); // Handle this case
    }
  }

  toggleNav() {
    const sidenavElement = this.el.nativeElement.querySelector('#mySidenav');
    const currentWidth = getComputedStyle(sidenavElement).width;

    if (currentWidth === '0px' || !currentWidth) {
      this.renderer.setStyle(sidenavElement, 'width', '250px');
    } else {
      this.renderer.setStyle(sidenavElement, 'width', '0');
    }
  }
  signOut() {
    console.log('Logout button clicked');
    // localStorage.clear();
    // sessionStorage.clear();
    this.authService.signOut();
    this.router.navigate(['/']);
  }
}
