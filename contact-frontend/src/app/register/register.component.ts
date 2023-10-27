import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  providers:[HttpClient]
})
export class RegisterComponent {
  firstName:string="";
  lastName:string="";
  email:string="";
  password:string="";
constructor(private http: HttpClient )
  {
  }
  register()
  {
     console.log("hiiiiii");
    
    let bodyData = {
      "firstname" : this.firstName,
      "lastname":this.lastName,
      "email" : this.email,
      "password" : this.password
    };
    this.http.post("http://localhost:8082/api/v1/auth/register",bodyData,{responseType: 'text'}).subscribe((resultData: any)=>
    {
        console.log(resultData);
        alert("Employee Registered Successfully");
    });

  }
}
