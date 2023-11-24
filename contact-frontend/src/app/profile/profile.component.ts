import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { TokenService } from '../service/token.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ToasterService } from '../service/toaster.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})

export class ProfileComponent {
   Name:string="";
   firstName:string="";
   email:string="";
   gender:string="";
   lastName:string="";
  user = {
    firstName: '',
    lastName: '',
    email: '',
    phoneno: '',
    gender: '',
    address: '',
    
  };
  user_id!:number;
  selectedFile: File | null = null;
  isButtonDisabled:boolean=false;

  constructor(
    private http: HttpClient,
    private manipulateuser: ManipulateUserService,
    private tokenservice: TokenService,
    private router:Router,
    private messageService: MessageService
  
  ) {}

  ngOnInit(){
  // const yourAuthToken = localStorage.getItem('token');
  const userid =localStorage.getItem('user_id');
  console.log("user_id",userid);
 
  if(userid!==null){
    const userId =parseInt(userid,10);
    this.tokenservice.getUserName().subscribe((data)=>{
      this.user=data;
      this.firstName =data.firstName;
      this.lastName = data.lastName;
      this.Name = data.firstName +" "+ data.lastName;
      this.email = data.email;
      this.gender=data.gender
      console.log("user",this.user);
      
    })
  }
  else{
    console.error("user_id is null in local storage");
  }
}

onFileSelected(event: any) {
  this.selectedFile = event.target.files[0] as File;
}


capitalizeFirstLetter(name: string): string {
  return name.charAt(0).toUpperCase() + name.slice(1);
}

onSubmit(userForm: NgForm) {
  this.isButtonDisabled = true;
  
  if (userForm.valid) {
    const userId = localStorage.getItem('user_id');
    if (userId !== null) {
      const parsedUserId = parseInt(userId, 10);
      if (!isNaN(parsedUserId)) {
        this.manipulateuser.updateUser(this.user, parsedUserId,).subscribe({
          next: (res) => {
            console.log(res);
            //  this.uploadProfilePicture(parsedUserId);
             
            this.messageService.add({
              severity: 'success',
              summary: 'Success',
              detail: 'Profile Updated Successfully',
            });
          },
          error: (err) => {
            console.log(err);
          },
        });
      } else {
        console.error("Invalid user_id in localStorage");
      }


      } 
    } else {
      console.error("user_id is null in localStorage");
    }
  }
}



