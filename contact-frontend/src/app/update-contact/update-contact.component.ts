import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { faCoffee } from '@fortawesome/free-solid-svg-icons';
import { ActivatedRoute, Router } from '@angular/router';
import { AddServiceService } from '../service/add-service.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { Injectable } from '@angular/core';
import { Toast } from 'primeng/toast';

import { compileNgModule } from '@angular/compiler';
import { ToasterService } from '../service/toaster.service';

@Component({
  selector: 'app-update-contact',
  templateUrl: './update-contact.component.html',
  styleUrls: ['./update-contact.component.scss'],
})
@Injectable()
export class UpdateContactComponent {
  faCoffee = faCoffee;
  contact = {
    firstname: '',
    lastname: '',
    email: '',
    work: '',
    phoneno: '',
    gender: '',
    description: '',
  };
  contact_id!: number;
  constructor(
    private route: ActivatedRoute,
    private addService: AddServiceService,
    private router: Router,
    private messageService: MessageService,
    private toasterService: ToasterService
  ) {
    this.route.params.subscribe((params) => {
      const contactId = +params['id']; // Convert to a number if needed
      this.contact_id = contactId;
      // Fetch the contact details by ID and prepopulate the form
      this.addService.getContactById(contactId).subscribe((data) => {
        this.contact = data; // Update 'contact' with fetched data
      });
    });
  }

  onSubmit(contactForm: NgForm) {
    if (contactForm.valid) {
      this.addService.updateContact(this.contact, this.contact_id).subscribe({
        next: (res) => {
          //console.log(res);

          setTimeout(() => {
            this.router.navigateByUrl('/api/v1/dashboard');
          }, 1500);
          this.toasterService.showContactUpdated();
        },
        error: (err) => {
          //console.log(err);
          // Handle the error
        },
      });
    } else {
      //this.messageService.add()
    }
  }
}
