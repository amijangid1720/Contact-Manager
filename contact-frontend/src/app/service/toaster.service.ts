import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class ToasterService {
  constructor(private messageService: MessageService) {}
 
  showContactUpdated() {
    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Contact Updated '
    });
  }

  showContactDeleted(){
    this.messageService.add({
      severity: 'info',
      summary: 'Deleted',
      detail: 'Contact Deleted ',
    })
  }
  showContactAdded() {
    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Contact Added ',
    });
  }
 
}
