import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { faCoffee } from '@fortawesome/free-solid-svg-icons';
import { ActivatedRoute, Router } from '@angular/router';
import { AddServiceService } from '../service/add-service.service';

@Component({
  selector: 'app-update-contact',
  templateUrl: './update-contact.component.html',
  styleUrls: ['./update-contact.component.scss'],
})
export class UpdateContactComponent {
  faCoffee = faCoffee;
  contact={
    firstname:'',
    lastname:'',
    email:'',
    work:'',
    phoneno:'',
    gender:'',
    description:''

  };
  contact_id!:number;
  constructor(
    private route: ActivatedRoute,
    private addService: AddServiceService,
    private router:Router
  ) {
    const yourAuthToken = localStorage.getItem('token');
    this.route.params.subscribe((params) => {
      const contactId = +params['id']; // Convert to a number if needed
      this.contact_id=contactId;
      // Fetch the contact details by ID and prepopulate the form
      if(yourAuthToken!=null)
      {
        this.addService.getContactById(contactId,yourAuthToken).subscribe((data) => {
          
          
          this.contact = data; // Update 'contact' with fetched data
          
        });
      }else {
        console.log('token not found');
      }
     
    });
  }
  onSubmit(contactForm:NgForm) {
    if (contactForm.valid) {
      const yourAuthToken = localStorage.getItem('token');
      if(yourAuthToken!=null)
      {
        
        this.addService.updateContact(this.contact,yourAuthToken,this.contact_id).subscribe({
          next: (res) => {
            console.log(res);
            this.router.navigateByUrl('/api/v1/dashboard');
          },
          error: (err) => {
            console.log(err);
            // Handle the error
          },
        });
      }else{
        console.log("Token not found")
      }
     
    } else {
      alert("Invalid Updates !!!");
    }
  }
}
