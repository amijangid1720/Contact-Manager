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
        name: contact.firstname + ' ' + contact.lastname,
        email: contact.email,
        phoneno: contact.phoneno,
      }));
    });
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
