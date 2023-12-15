import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ManipulateUserService } from '../service/manipulate-user.service';

@Component({
  selector: 'app-user-details-form',
  templateUrl: './user-details-form.component.html',
  styleUrls: ['./user-details-form.component.scss'],
})
export class UserDetailsFormComponent {
  user = {
    firstName: '',
    lastName: '',
    email: '',
    phoneno: '',
    gender: '',
    address: '',
    detailsFilled: false,
  };
  user_id!: number;
  isButtonDisabled: boolean = false;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private manipulateUserService: ManipulateUserService
  ) {}
  ngOnInit() {
    const userid = localStorage.getItem('user_id');
    //console.log("user_id", userid);

    if (userid !== null) {
      const userId = parseInt(userid, 10);
      this.manipulateUserService.getUserById(userId).subscribe((data) => {
        this.user = data;
        //console.log("user", this.user);
      });
    } else {
      console.error('user_id is null in localStorage');
    }
  }

  onSubmit(userForm: NgForm) {
    console.log("11111");
    
    this.isButtonDisabled = true;
    if (userForm.valid) {
      const userId = localStorage.getItem('user_id');
      if (userId !== null) {
        const parsedUserId = parseInt(userId, 10);
        if (!isNaN(parsedUserId)) {
          console.log("@2222");
          
          this.manipulateUserService
            .updateUser(this.user, parsedUserId)
            .subscribe({
              next: (res) => {
                console.log(res);
                this.user.detailsFilled = true;

                this.manipulateUserService
                  .updateDetailsFilled(parsedUserId)
                  .subscribe({
                    next: (updateRes) => {
                      console.log(updateRes);
                    },
                    error: (updateErr) => {
                      console.error(updateErr);
                    },
                  });

                setTimeout(() => {
                  this.router.navigateByUrl('dashboard');
                }, 1500);
              },
              error: (err) => {
                //console.log(err);
              },
            });
        } else {
          console.error('Invalid user_id in localStorage');
        }
      } else {
        console.error('user_id is null in localStorage');
      }
    }
  }
}   
