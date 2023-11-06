import { Component, ElementRef, Renderer2 } from '@angular/core';
import { StorageService } from '../service/storage.service';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-navbar1',
  templateUrl: './navbar1.component.html',
  styleUrls: ['./navbar1.component.scss']
})
export class Navbar1Component {constructor(private router: Router,
  private storageService: StorageService,
 private authService: AuthService
 ,
 private renderer: Renderer2, private el: ElementRef) {}

 openNav(){
  const sidenavElement = this.el.nativeElement.querySelector('#mySidenav');
  this.renderer.setStyle(sidenavElement, 'width', '250px');
 }

 closeNav() {
  const sidenavElement = this.el.nativeElement.querySelector('#mySidenav');
  this.renderer.setStyle(sidenavElement, 'width', '0');
}

signOut() {
  console.log('Logout button clicked');
  this.authService.signOut();
  this.router.navigate(['/']);
}

}
