import { Component, OnInit } from '@angular/core';

import { faPen,faTrashCan,faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
import { HttpClient } from '@angular/common/http';
// import { map } from 'rxjs';
// import { contacts } from '../model/contacts';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { Router } from '@angular/router';
import { AddServiceService } from '../service/add-service.service';

@Component({
  selector: 'app-contact-table',
  templateUrl: './contact-table.component.html',
  styleUrls: ['./contact-table.component.scss'],
})
export class ContactTableComponent  implements OnInit{
  faPen = faPen;
  faTrashCan = faTrashCan;
  faArrowRightFromBracket = faArrowRightFromBracket;
  contacts!: any[];
    const data1:any | undefined;
  constructor(
    private http: HttpClient,
    private manipulateuser: ManipulateUserService,
    private router: Router,
    private addService: AddServiceService
  ) {}

  ngOnInit() {
    const yourAuthToken = localStorage.getItem('token');
    if (yourAuthToken != null) {
      this.manipulateuser
        .getContacts(yourAuthToken)
        .subscribe((data: any[]) => {
          console.log(data);
       this.data1= data;

          this.contacts = data.map((contact) => ({
            id: contact.id,
            name: contact.firstname + ' ' + contact.lastname,
            email: contact.email,
            phoneno: contact.phoneno,
          }));
        });
    } else {
      console.log('token not found');
    }
  }

  updateContact(contact: object, id: number) {
  
    this.router.navigate(['update-contact', id]);
  }
}

  onDeleteContact(contactId: string) {
    const yourAuthToken = localStorage.getItem('token');
   const dub = this.data1[contactId].id;

    if (yourAuthToken != null) {
      this.manipulateuser.deleteContact(dub, yourAuthToken).subscribe(
        () => {
          console.log(`Contact with ID ${dub} deleted successfully.`);

          window.location.reload();
          // Perform any additional actions after successful deletion
        },
        error => {
          console.error('Error deleting contact:', error);
        }
      );
    }
     else {
      console.log('Token not found');
    }

}
}