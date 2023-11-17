import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-user-details-form',
  templateUrl: './user-details-form.component.html',
  styleUrls: ['./user-details-form.component.scss']
})
export class UserDetailsFormComponent {
  user = {
    firstName: '',
    lastName: '',
    email: '',
    phoneno: '',
    gender: '',
    address: '',
  };
  user_id!: number;
  isButtonDisabled: boolean = false;
  constructor(
    private route:ActivatedRoute,
    private router: Router,
    private manipulateUserService: ManipulateUserService
   
  ) {}
  ngOnInit(){
  const userid =localStorage.getItem('user_id');
   console.log("user_id", userid);

   if (userid !== null) {
   const userId = parseInt(userid, 10);
   this.manipulateUserService.getUserById(userId).subscribe((data)=>{
    this.user = data;
    console.log("user", this.user);
    
   })
   }    else {
  console.error("user_id is null in localStorage");
   }
}

onSubmit(userForm: NgForm) {
  this.isButtonDisabled = true;
  if (userForm.valid) {
    const userId = localStorage.getItem('user_id');
    if (userId !== null) {
      const parsedUserId = parseInt(userId, 10);
      if (!isNaN(parsedUserId)) {
        this.manipulateUserService.updateUser(this.user, parsedUserId).subscribe({
          next: (res) => {
            console.log(res);

            setTimeout(() => {
             this.router.navigateByUrl('dashboard');
            }, 1500);
          },
          error: (err) => {
            console.log(err);
          },
        });
      } else {
        console.error("Invalid user_id in localStorage");
      }
    } else {
      console.error("user_id is null in localStorage");
    }
  }
}

}