import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { TokenService } from '../service/token.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})

export class ProfileComponent {
  contacts!: any[];
  data1!: any;
  Name:string="";
  firstName:string="";
  email:string="";
  phoneno:string="";
  gender:string="";
  lastName:string="";
  address:string="";


  constructor(
    private http: HttpClient,
    private manipulateuser: ManipulateUserService,
    private tokenservice: TokenService
  
  ) {}

  ngOnInit(){
  const yourAuthToken = localStorage.getItem('token');
 
  if (yourAuthToken !== null) { // Check if it's not null
    this.tokenservice.getUserName().subscribe({
      next: (res) => {
        console.log("RESULT", res);
        this.firstName = res.firstName;
        this.lastName = res.lastName;
        this.address = res.address;
        this.Name = res.firstName + ' ' + res.lastName;
        this.email = res.email;
        this.phoneno = res.phoneno;
        this.gender = res.gender;
      },
      error: (err) => {
        console.log(err);
      },
    });
  } else {
    console.error("Token not found in localStorage."); // Handle this case
  }
}
}
  

