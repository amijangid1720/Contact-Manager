import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AddServiceService } from '../service/add-service.service';
import { Router } from '@angular/router';
import { faCoffee,faCircleChevronLeft} from '@fortawesome/free-solid-svg-icons';
import { } from '@fortawesome/free-solid-svg-icons';
import { ToasterService } from '../service/toaster.service';
import { MessageService } from 'primeng/api';
import { environment } from '../environment';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']
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
  contact={
   firstname:"",
   lastname:"",
   email:"",
   work:"",
   phoneno:"",
   gender:"",
   description:""
  };
  onSubmit(contactForm: NgForm) {
    if (contactForm.valid) {
      this.addService.addContact(this.contact).subscribe({
        next: (res) => {
          console.log(res);
          this.toasterService.showContactAdded();
          setTimeout(()=>{
            this.router.navigateByUrl('/dashboard');
          },1000)
          
        },
        error: (err) => {
          console.log(err);
        },
      });

    } else {
      // Handle form validation errors
    }
  }
}
