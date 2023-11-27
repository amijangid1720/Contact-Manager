import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component } from '@angular/core';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { TokenService } from '../service/token.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { ToasterService } from '../service/toaster.service';
import { environment } from '../environment';
import { Observable } from 'rxjs';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent {
  Name: string = '';
  firstName: string = '';
  email: string = '';
  gender: string = '';
  lastName: string = '';
  userProfilePicture: string = '';
  user = {
    firstName: '',
    lastName: '',
    email: '',
    phoneno: '',
    gender: '',
    address: '',
    profilePicture: '',
  };
  user_id!: number;
  selectedFile: File | null = null;
  isButtonDisabled: boolean = false;
  defaultProfilePictureMale: string = '../../assets/boy.png';
  defaultProfilePictureFemale: string = '../../assets/user3.png';
  constructor(
    private http: HttpClient,
    private manipulateuser: ManipulateUserService,
    private tokenservice: TokenService,
    private router: Router,
    private toasterService: ToasterService,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    // const yourAuthToken = localStorage.getItem('token');
    const userid = localStorage.getItem('user_id');
    console.log('user_id', userid);

    if (userid !== null) {
      const userId = parseInt(userid, 10);
      this.tokenservice.getUserName().subscribe((data) => {
        this.user = data;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.Name = data.firstName + ' ' + data.lastName;
        this.email = data.email;
        this.userProfilePicture = data.profilePicture;
        this.gender = data.gender;
      });
    } else {
      console.error('user_id is null in local storage');
    }
  }

  onSubmit(userForm: NgForm) {
    this.isButtonDisabled = true;
    if (userForm.valid) {
      const userId = localStorage.getItem('user_id');
      if (userId !== null) {
        const parsedUserId = parseInt(userId, 10);
        if (!isNaN(parsedUserId)) {
          this.manipulateuser.updateUser(this.user, parsedUserId).subscribe({
            next: (res) => {
              console.log(res);
              // this.uploadProfilePicture(parsedUserId);
              this.toasterService.showUserUpdated();
            },
            error: (err) => {
              console.log(err);
            },
          });
        } else {
          console.error('Invalid user_id in localStorage');
        }
      }
    } else {
      console.error('user_id is null in localStorage');
    }
  }

  onFileSelected(event: any) {
    const files: FileList | null = event.target.files;

    if (files && files.length > 0) {
      const selectedFile: File = files[0];
      console.log('Selected File:', selectedFile);

      const userId = localStorage.getItem('user_id');
      if (userId !== null) {
        const parsedUserId = parseInt(userId, 10);
        this.uploadProfilePicture(parsedUserId, selectedFile).subscribe(
          (data) => {
             this.userProfilePicture = data.url;
             console.log("huggrgrrt");
             
            console.log('Image Url',data.url);
          }
        );
      }
    }
  }

  onUpload(event: any) {
    for (const file of event.files) {
      this.messageService.add({
        severity: 'info',
        summary: 'File Uploaded',
        detail: `${file.name} has been uploaded successfully.`,
      });
    }
  }
  uploadProfilePicture(userid: number, file: File):Observable<any> {
    const formData: FormData = new FormData();
    formData.append('image', file);
    console.log(formData);

    const headers = new HttpHeaders();
    // headers.set('Content-Type', 'multipart/form-data');
    headers.append('Content-Type', 'multipart/form-data');

 console.log("call");
 
   return this.http.post(
      `${environment.backendUrl}/api/v1/contacts/upload-profile/${userid}`,
      formData,
      { headers }
    )
  }
}