import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { faCoffee } from '@fortawesome/free-solid-svg-icons';
import { AddServiceService } from '../service/add-service.service';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']

})
export class AddComponent {

  constructor(
    private router: Router,
    private addService:AddServiceService,
   
  ) {}
  faCoffee = faCoffee;
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
      // Send the 'contact' object to your API using a service
      console.log(contactForm); // You can remove this line
      const yourAuthToken = localStorage.getItem('token'); // Try to get the token
      if (yourAuthToken !== null) { // Check if it's not null
        this.addService.addContact(this.contact, yourAuthToken).subscribe({
          next: (res) => {
            console.log(res);
            this.router.navigateByUrl('api/v1/dashboard');
          },
          error: (err) => {
            console.log(err);
          },
        });
      } else {
        console.error("Token not found in localStorage."); // Handle this case
      }
    } else {
      // Handle form validation errors
    }
  }
}
