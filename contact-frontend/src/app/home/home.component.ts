import { Component } from '@angular/core';
import { TokenService } from '../service/token.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  constructor( private tokenservice: TokenService) {}
  loggedin!:boolean;

  ngOnInit()
  {
    this.loggedin= this.tokenservice.isLoggedin();
    console.log();
    
  }
}
