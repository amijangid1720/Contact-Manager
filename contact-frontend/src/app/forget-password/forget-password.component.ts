import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.scss']
})
export class ForgetPasswordComponent {

  constructor(private authService:AuthService){}
  
  user ={
    email:'',
  }
  onSubmit(userForm:NgForm){
    console.log(userForm);  
    console.log("submitted");

    if(userForm.valid){
      this.authService.forgetpassword(this.user.email).subscribe((result:any) =>{
             console.log("result:", result);
             
      })
    }
    

  }

}
