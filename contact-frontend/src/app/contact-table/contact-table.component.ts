import { Component, OnInit } from '@angular/core';

import {
  faPen,
  faTrashCan,
  faArrowRightFromBracket,
  faCaretUp,
  faCaretDown
} from '@fortawesome/free-solid-svg-icons';
import { HttpClient } from '@angular/common/http';

import { ManipulateUserService } from '../service/manipulate-user.service';
import { Router } from '@angular/router';
import { AddServiceService } from '../service/add-service.service';
import { ToasterService } from '../service/toaster.service';
import { MessageService } from 'primeng/api';
import { ConfirmationService, ConfirmEventType } from 'primeng/api';



@Component({
  selector: 'app-contact-table',
  templateUrl: './contact-table.component.html',
  styleUrls: ['./contact-table.component.scss'],
  providers: [ConfirmationService, MessageService],
})
export class ContactTableComponent implements OnInit {
  faPen = faPen;
  faTrashCan = faTrashCan;
  faCaretUp =faCaretUp;
  faCaretDown = faCaretDown;
  faArrowRightFromBracket = faArrowRightFromBracket;
  contacts!: any[];
  data1!: any;
  searchTerm:string="";
  first: number = 0;
  rows: number = 10;
  loading: boolean = false;
  sortField: string = 'name';
  sortOrder: string = 'asc';

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
    this.loadContacts();
  }
 
  
  loadContacts() {
    this.manipulateuser.getContacts(
      this.first / this.rows,
      this.rows
    ).subscribe(
      (response: any) => {
        if (response && Array.isArray(response.content)) {
          // Map contacts without modifying the original response
          const mappedContacts = response.content.map((contact: any) => ({
            id: contact.id,
            name: contact.firstname + ' ' + contact.lastname,
            email: contact.email,
            phoneno: contact.phoneno,
            work: contact.work,
          }));
  
          // Sort the mappedContacts array based on the selected field and order
          mappedContacts.sort((a: any, b: any) => {
            const valueA = a[this.sortField];
            const valueB = b[this.sortField];
  
            // Ensure the values are of string type before using localeCompare
            const strValueA = String(valueA);
            const strValueB = String(valueB);
  
            return this.sortOrder === 'asc' ? strValueA.localeCompare(strValueB) : strValueB.localeCompare(strValueA);
          });
  
          // Update the contacts array after mapping and sorting
          this.contacts = mappedContacts;
  
          console.log('Mapped and Sorted Contacts:', this.contacts);
        } else {
          console.error('Unexpected API response format. Data:', response);
        }
  
        this.loading = false;
      },
      (error) => {
        console.error('Error fetching contacts:', error);
        this.loading = false;
      }
    );
  }
  
  

  setSortField(field: string) {
    // Toggle the sort order if the same field is clicked again
    if (this.sortField === field) {
      this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    } else {
      // Set the default sort order to 'asc' when a new field is selected
      this.sortOrder = 'asc';
    }
  
    this.sortField = field;
    this.loadContacts(); // Refresh contacts with the new sorting field and order
  }


  onScroll(event: any) {
    // Check if the user has scrolled to the bottom


    console.log('Scroll event triggered!');
    console.log('Scroll top:', event.target.scrollTop);
    console.log('Container height:', event.target.offsetHeight);
    console.log('Total content height:', event.target.scrollHeight);
  
    if (event.target.offsetHeight + event.target.scrollTop >= event.target.scrollHeight) {
      this.first += this.rows; // Increment the offset
      this.loadContacts(); // Load more contacts
    }
  }



    onPageChange(event: any) {
        this.first = event.first;
        this.rows = event.rows;
        console.log(this.first);
        console.log(this.rows);
       this.loadContacts();
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
              severity: 'warn',
              summary: 'Cancelled',
              detail: 'You have cancelled',
            });
            break;
        }
      },
    });
  }
}
