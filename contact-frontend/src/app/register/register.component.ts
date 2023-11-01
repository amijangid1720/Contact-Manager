import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { faCoffee } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  providers:[HttpClient]
})
export class RegisterComponent {
  faCoffee = faCoffee;
  user={
  firstName:"",
  lastName:"",
  email:"",
  password:"",
  phoneNo:"",
  gender:"",
 address:""
  }
userForm: any;
constructor(private http: HttpClient )
  {
  }
  // register()
  // {
  //    console.log("hiiiiii");
    
  //   let bodyData = {
  //     "firstname" : this.firstName,
  //     "lastname":this.lastName,
  //     "email" : this.email,
  //     "password" : this.password,
  //     "phoneNo" : this.phoneNo,
  //     'gender' : this.gender,
  //      'address':this.address,
  //   };
  //   this.http.post("http://localhost:8082/api/v1/auth/register",bodyData,{responseType: 'text'}).subscribe((resultData: any)=>
  //   {
  //       console.log(resultData);
  //       alert("Employee Registered Successfully");
  //   });

  // }
  OnSubmit(userForm: NgForm){
   if(userForm.valid) {
      console.log(userForm);
      this.http.post("http://localhost:8082/api/v1/auth/register",this.user,{responseType: 'text'}).subscribe((resultData: any)=>
       {
             console.log(resultData);
             alert("Regeistration Successful");
   });
  }
  else{
           alert("Registraion was unsuccessfull!!") ;
  }
}
}
