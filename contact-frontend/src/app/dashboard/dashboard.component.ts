import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from '../service/storage.service';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent {
  constructor(private router: Router,
    private storageService: StorageService,
   private authService: AuthService) {}
  signOut() {
    console.log('Logout button clicked');
    // localStorage.clear();
    // sessionStorage.clear(); 
    this.authService.signOut();
    this.router.navigate(['/']);
  }
}
