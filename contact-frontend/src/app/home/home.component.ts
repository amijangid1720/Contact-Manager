import { Component } from '@angular/core';
import { TokenService } from '../service/token.service';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  constructor( private tokenservice: TokenService,
    private authService:AuthService,
    private router:Router) {}

  ngOnInit()
  {
   
  }

  isLoggedout():boolean{
    if(this.tokenservice.fetchToken())
    return false;
    else
    return true;
  }
 
}
