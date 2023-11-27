import { Component, OnInit } from '@angular/core';

import {
  faPen,
  faTrashCan,
  faArrowRightFromBracket,
  faCaretUp,
  faCaretDown,
  faSearch,
  faMagnifyingGlass,
  faStar,
  faHeart,
  faUserSlash,
  faUserPlus,
} from '@fortawesome/free-solid-svg-icons';
import { HttpClient, HttpParams } from '@angular/common/http';

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
  faCaretUp = faCaretUp;
  faCaretDown = faCaretDown;
  faHeart = faHeart;
  faSearch = faSearch;
  faUserSlash = faUserSlash;
  faUserPlus = faUserPlus;
  faMagnifyingGlass = faMagnifyingGlass;
  faArrowRightFromBracket = faArrowRightFromBracket;
  contacts!: any[];
  data1!: any;
  searchTerm: string = '';
  first: number = 0;
  rows: number = 5;
  totalRecords!: number;
  loading: boolean = false;
  sortField: string = 'name';
  sortOrder: string = 'asc';
  isContactsEmpty: boolean = false;

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
    this.loading = true;
    //console.log('Loading started');

    setTimeout(() => {
      this.manipulateuser
        .getContacts(this.first / this.rows, this.rows)
        .subscribe(
          (response: any) => {
            if (response && Array.isArray(response.contacts)) {
              const mappedContacts = response.contacts.map((contact: any) => ({
                id: contact.id,
                name: contact.firstname + ' ' + contact.lastname,
                email: contact.email,
                phoneno: contact.phoneno,
                work: contact.work,
                gender: contact.gender,
                favorite: contact.favorite,
              }));

              this.totalRecords = response.totalContacts;
              //console.log("total:" ,this.totalRecords);
              this.data1 = mappedContacts;

              // Sort the mappedContacts array based on the selected field and order
              mappedContacts.sort((a: any, b: any) => {
                const valueA = a[this.sortField];
                const valueB = b[this.sortField];

                // Ensure the values are of string type before using localeCompare
                const strValueA = String(valueA);
                const strValueB = String(valueB);

                return this.sortOrder === 'asc'
                  ? strValueA.localeCompare(strValueB)
                  : strValueB.localeCompare(strValueA);
              });
              // Update the contacts array after mapping and sorting
              this.contacts = mappedContacts;
              //console.log('Mapped and Sorted Contacts:', this.contacts);
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
    }, 500);
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

  onPageChange(event: any) {
    this.first = event.first;
    this.rows = event.rows;
    //console.log(this.first);
    //console.log(this.rows);
    this.loadContacts();
  }

  setdata(contact: any) {
    this.contacts = contact.map((contact: any) => ({
      id: contact.id,
      name: contact.firstname + ' ' + contact.lastname,
      firstname: contact.firstname,
      lastname: contact.lastname,
      email: contact.email,
      phoneno: contact.phoneno,
      work: contact.work,
    }));
  }

  searchContacts() {
    if (this.searchTerm) {
      const params = new HttpParams()
        .set('page', this.first.toString())
        .set('size', this.rows.toString());
  
      // Make an HTTP request to fetch matching contacts with pagination
      this.http
        .get<any>(
          `${environment.backendUrl}/api/v1/contacts/search/${this.searchTerm}`,
          { params }
        )
        .subscribe(
          (response) => {
            console.log(response);
            this.contacts = response.contacts; // Update the contacts array with the search results
            this.totalRecords = response.totalContacts;
            this.setdata(this.contacts);
            console.log('This is this.contacts');
            console.log(this.contacts);
            console.log(this.contacts.length);
          },
          (error) => {
            console.error('Error fetching contacts:', error);
            this.messageService.add({
              severity: 'error',
              summary: 'Error',
              detail: 'Could not find any matching contact',
            });
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
    //console.log(contactId);

    this.confirmationService.confirm({
      accept: () => {
        const dub = this.data1[contactId].id;
        console.log(dub);
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
            });
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
  toggleFavorite(contact: any): void {
    contact.favorite = !contact.favorite;
    console.log('favorite');

    // Update the favorite status on the server
    this.addService
      .toggleFavorite(contact.id, contact.favorite)
      .subscribe(() => {
        // Optional: Show a success message or handle other actions after a successful update
        console.log('favorite');
      });
  }
}
