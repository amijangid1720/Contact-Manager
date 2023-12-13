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
  faCloudArrowDown,
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
import { AuthService } from '../service/auth.service';
import { Token } from '@angular/compiler';
import { LoginComponent } from '../login/login.component';
// import { TryComponent } from '../try/try.component';

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
  faCloudArrowDown = faCloudArrowDown;
  faMagnifyingGlass = faMagnifyingGlass;
  faArrowRightFromBracket = faArrowRightFromBracket;
  contacts!: any;
  data1!: any;
  searchTerm: string = '';
  filterTerm:string='';
  first: number = 0;
  rows: number = 5;
  totalRecords!: number;
  loading: boolean = false;
  sortField: string = 'name';
  sortOrder: string = 'asc';
  isContactsEmpty: boolean = false;
  showDropdown = false;
  Allcontacts: any[] = [];

  constructor(
    private http: HttpClient,
    private manipulateuser: ManipulateUserService,
    private router: Router,
    private addService: AddServiceService,
    private messageService: MessageService,
    private toasterService: ToasterService,
    private confirmationService: ConfirmationService,
    private authService:AuthService,
  ) {
    console.log("hiiii");
    
  }

  ngOnInit() {
    this.loadContacts();
  
    
  }
  
  selectTerm(term: string): void {
    this.filterTerm = term;
    // You can do additional processing here based on the selected term
    
  } 

  loadContacts() {
    this.loading = true;
    //console.log('Loading started');

   
      this.manipulateuser
        .getContacts(this.first / this.rows, this.rows)
        .subscribe(
          (response: any) => {
            if (response && Array.isArray(response.contacts)) {
              const mappedContacts = response.contacts.map((contact: any) => ({
                id: contact.id,
                firstname: contact.firstname ,
                lastname: contact.lastname,
                email: contact.email,
                phoneno: contact.phoneno,
                work: contact.work,
                gender: contact.gender,
                favorite: contact.favorite,
              }));

              this.totalRecords = response.totalContacts;

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
      // firstname: contact.firstname + ' ' + contact.lastname,
      firstname: contact.firstname,
      lastname: contact.lastname,
      email: contact.email,
      phoneno: contact.phoneno,
      work: contact.work,
    }));
  }

  searchContacts() {
    if (this.searchTerm && this.filterTerm) {
      const params = new HttpParams()
        .set('page', this.first.toString())
        .set('size', this.rows.toString());
  
      // Make an HTTP request to fetch matching contacts with pagination
      this.http
        .get<any>(
          `${environment.backendUrl}/api/v1/contacts/search/${this.searchTerm}/${this.filterTerm}`,
          { params }
        )
        .subscribe(
          (response) => {
            console.log(response, "res");
            this.contacts = response.contacts; // Update the contacts array with the search results
           
            
            this.totalRecords = response.totalContacts;
          
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
   btn()
   {
    console.log("clicked");
    
   }

   uploadContacts(){
    this.manipulateuser.uploadContacts(this.contacts).subscribe(
      (response) => {
        console.log('Contacts uploaded successfully', response);
        this.messageService.add({
          severity: 'success',
          summary: 'Uploaded',
          detail: 'Contacts added to Drive',
        });
        
       //you can trigger the additional logic to upload to Google Drive
      },
      (error) => {
        console.error('Error uploading contacts', error);
        // Handle error scenarios
        this.messageService.add({
          severity: 'alert',
          summary: 'Failed',
          detail: 'Failed to upload Conatcts Drive',
        });
      }
    );
   }
    
  // uploadContacts() {
  //   const id = localStorage.getItem('user_id');
  //   if (id !== null) {
  //     const userid = parseInt(id, 10);
  //     this.manipulateuser.getallContacts(userid).subscribe(
  //       (response: any) => {
  //         console.log("response ", response);
  //         this.Allcontacts = response;
          
  //         this.manipulateuser.uploadContacts(this.Allcontacts).subscribe(
  //           (response) => {
  //             console.log("contacts uploaded successfully", response);
  //             this.messageService.add({
  //               severity: 'success',
  //               summary: 'Uploaded',
  //               detail: 'Contacts added to drive',
  //             });
  //           },
  //           (error) => {
  //             console.error('Error uploading contacts', error);
  //             // Handle error scenarios
  //             this.messageService.add({
  //               severity: 'alert',
  //               summary: 'Failed',
  //               detail: 'Failed to upload Contacts Drive',
  //             });
  //           }
  //         );
  //       },
  //       (error) => {
  //         console.error('Error fetching contacts', error);
  //         // Handle error scenarios
  //         this.messageService.add({
  //           severity: 'error',
  //           summary: 'Failed',
  //           detail: 'Failed to fetch Contacts',
  //         });
  //       }
  //     );
  //   }
  // }
  

   downloadContacts(){
    this.manipulateuser.downloadContacts().subscribe(
      (response) => {
        console.log('Contacts downloaded successfully', response);
        this.messageService.add({
          severity: 'success',
          summary: 'Downoaded',
          detail: 'Contacts Downloaded from drive',
        });
        
       //you can trigger the additional logic to upload to Google Drive
      },
      (error) => {
        console.error('Error uploading contacts', error);
        // Handle error scenarios
        this.messageService.add({
          severity: 'alert',
          summary: 'Failed',
          detail: 'Failed to download Conatcts  from Drive',
        });
      }
    );
   }


   toggleDropdown() {
    this.showDropdown = !this.showDropdown;
  }



}
