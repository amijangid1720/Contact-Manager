import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-user-details-form',
  templateUrl: './user-details-form.component.html',
  styleUrls: ['./user-details-form.component.scss']
})
export class UserDetailsFormComponent {
  user = {
    firstname: '',
    lastname: '',
    email: '',
    work: '',
    phoneno: '',
    gender: '',
    description: '',
  };
  user_id!: number;
  constructor(
    private route:ActivatedRoute,
    private router: Router,
   
  ) {
   const userid = localStorage.getItem('user_id');
   console.log("user", userid);
   
  }

  onSubmit(userForm:NgForm){
    if(userForm.valid){
    
    }
  }
}
