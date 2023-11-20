import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { TokenService } from '../service/token.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';

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
  
  constructor(
    private http: HttpClient,
    private manipulateuser: ManipulateUserService,
    private tokenservice: TokenService,
    private router:Router,
  
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
      this.Name = data.firstName + data.lastName;
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
onSubmit(userForm: NgForm) {
  
  if (userForm.valid) {
    const userId = localStorage.getItem('user_id');
    if (userId !== null) {
      const parsedUserId = parseInt(userId, 10);
      if (!isNaN(parsedUserId)) {
        this.manipulateuser.updateUser(this.user, parsedUserId,).subscribe({
          next: (res) => {
            console.log(res);
            // this.uploadProfilePicture(parsedUserId);
           
          },
          error: (err) => {
            console.log(err);
          },
        });
      } else {
        console.error("Invalid user_id in localStorage");
      }


      // if (this.selectedFile) {
      //   // Upload the selected file to the server
      //   const formData: FormData = new FormData();
      //   console.log(formData);
        
      //   formData.append('file', this.selectedFile, this.selectedFile.name);
    
      //   this.manipulateuser.uploadProfilePicture(formData).subscribe({
      //     next: (res) => {
      //       console.log('File uploaded successfully:', res);
      //       // Handle the response from the server if needed
      //     },
      //     error: (err) => {
      //       console.error('Error uploading file:', err);
      //       // Handle the error if needed
      //     },
      //   });
      } 
    } else {
      console.error("user_id is null in localStorage");
    }
  }
private uploadProfilePicture(userId: number) {
  if (this.selectedFile) {
    const formData: FormData = new FormData();
    formData.append('file', this.selectedFile, this.selectedFile.name);

    this.manipulateuser.uploadProfilePicture(formData,userId).subscribe(
      (res) => {
        console.log('File uploaded successfully:', res);
      },
      (err) => {
        console.error('Error uploading file:', err);
      }
    );
    }
 
}

}
  

