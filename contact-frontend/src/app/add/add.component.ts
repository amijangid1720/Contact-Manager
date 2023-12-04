import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AddServiceService } from '../service/add-service.service';
import { Router } from '@angular/router';
import {
  faCoffee,
  faCircleChevronLeft,
} from '@fortawesome/free-solid-svg-icons';
import {} from '@fortawesome/free-solid-svg-icons';
import { ToasterService } from '../service/toaster.service';
import { MessageService } from 'primeng/api';
import { environment } from '../environment';
@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss'],
})
export class AddComponent {
  constructor(
    private router: Router,
    private addService:AddServiceService,
    private toasterService:ToasterService,
    private messageService:MessageService
  ) {}
  faCoffee = faCoffee;
  faBackward = faCircleChevronLeft;
  contact = {
    firstname: '',
    lastname: '',
    email: '',
    work: '',
    phoneno: '',
    gender: '',
    description: '',
    categories:'',
    isFamily:false,
    isFriend:false,
    isColleague:false
  };
  searchTerm:string="";
  onSubmit(contactForm: NgForm) {
    console.log(contactForm);

    console.log("submitted Form !!");
    
    
    if (contactForm.valid) {
      const phoneno = this.contact.phoneno;
      const phone = parseInt(phoneno, 10);
      if(this.contact.categories==='family')
      {
        this.contact.isFamily=true;

      }else if(this.contact.categories==='friend')
      {
        this.contact.isFriend=true;
      }else{
        this.contact.isColleague=true;
      }

      this.addService
        .checkContact(this.contact.email, phone)
        .subscribe((resultData: any) => {
          //console.log(resultData, 'hjkh');

          if (resultData.emailExists || resultData.phoneExists) {
            if (resultData.emailExists && resultData.phoneExists) {
              this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Email And Phone Number Exists!',
              });
            } else if (resultData.emailExists) {
              this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Email Exists! ',
              });
            } else if (resultData.phoneExists) {
              this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Phone Number Exists! ',
              });
            }
          } else {
            console.log(this.contact);
            
            this.addService.addContact(this.contact).subscribe({
              next: (res) => {
                //console.log(res);
                this.toasterService.showContactAdded();
                setTimeout(() => {
                  this.router.navigateByUrl('/dashboard');
                }, 1000);
              },
              error: (err) => {
                console.log(err);
              },
            });
          }
        });
    } else {
      // handle errors
      console.log('error');
    }
  }
}
