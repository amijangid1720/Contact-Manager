import { Component, OnInit } from '@angular/core';

import {
  faPen,
  faTrashCan,
  faArrowRightFromBracket,
} from '@fortawesome/free-solid-svg-icons';
import { HttpClient } from '@angular/common/http';

import { ManipulateUserService } from '../service/manipulate-user.service';
import { Router } from '@angular/router';
import { AddServiceService } from '../service/add-service.service';
import { ToasterService } from '../service/toaster.service';
import { MessageService } from 'primeng/api';
import { ConfirmationService, ConfirmEventType } from 'primeng/api';
import { FormsModule } from '@angular/forms';
import { environment } from '../environment';

@Component({
  selector: 'app-contact-table',
  templateUrl: './contact-table.component.html',
  styleUrls: ['./contact-table.component.scss'],
  providers: [ConfirmationService, MessageService],
})
export class ContactTableComponent implements OnInit {
  faPen = faPen;
  faTrashCan = faTrashCan;
  faArrowRightFromBracket = faArrowRightFromBracket;
  contacts!: any[];
  data1!: any;
  searchTerm:string="";

  constructor(
    private http: HttpClient,
    private manipulateuser: ManipulateUserService,
    private router: Router,
    private addService: AddServiceService,
    private messageService: MessageService,
    private toasterService: ToasterService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit() {
    this.manipulateuser.getContacts().subscribe((data: any[]) => {
      console.log(data);
      this.data1 = data;

      this.contacts = data.map((contact) => ({
        id: contact.id,
        firstname: contact.firstname ,
        lastname: contact.lastname,
        email: contact.email,
        phoneno: contact.phoneno,
        work:contact.work
      }));
    });
  }

  searchContacts() {
    if (this.searchTerm) {
      // Make an HTTP request to fetch matching contacts
      this.http.get<any[]>(`${environment.backendUrl}/ap1/v1/contacts/search/${this.searchTerm}`).subscribe(
        (data) => {
          console.log(data)
          this.contacts = data; // Update the contacts array with the search results
          console.log("This is this.contacts");
          console.log(this.contacts);
          
          
        },
        (error) => {
          console.error('Error fetching contacts:', error);
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: 'Could not find any matching contact',
          })
        }
      );
    } else {
      // Handle when the search input is empty, e.g., display all contacts
      this.contacts = []; // Clear the search results
    }
  }

  updateContact(contact: object, id: number) {
    this.router.navigate(['update-contact', id]);
  }

  onDeleteContact(contactId: string) {
    this.confirmationService.confirm({
      accept: () => {
        const dub = this.data1[contactId].id;
        this.manipulateuser.deleteContact(dub).subscribe(
          () => {
            this.messageService.add({
              severity: 'info',
              summary: 'Deleted',
              detail: 'Contact Deleted',
            });
            setTimeout(() => {
              window.location.reload();
            }, 1500);
          },
          (error) => {
            console.error('Error deleting contact:', error);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Error deleting contact',
            })
          }
        );
      },
      reject: (type: ConfirmEventType) => {
        switch (type) {
          case ConfirmEventType.REJECT:
            this.messageService.add({
              severity: 'error',
              summary: 'Rejected',
              detail: 'You have rejected',
            });
            break;
          case ConfirmEventType.CANCEL:
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'You have cancelled',
            });
            break;
        }
      },
    });
  }
}
