import { Component, OnInit } from '@angular/core';

import { faUserPen,faTrashCan,faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';
import { HttpClient } from '@angular/common/http';
// import { map } from 'rxjs';
// import { contacts } from '../model/contacts';
import { ManipulateUserService } from '../service/manipulate-user.service';

@Component({
  selector: 'app-contact-table',
  templateUrl: './contact-table.component.html',
  styleUrls: ['./contact-table.component.scss'],
})
export class ContactTableComponent  implements OnInit{
  faUserPen = faUserPen;
  faTrashCan = faTrashCan;
  faArrowRightFromBracket = faArrowRightFromBracket;
  contacts!: any[];

  constructor(private http:HttpClient,
    private manipulateuser: ManipulateUserService) { }

  ngOnInit() {
    const yourAuthToken = localStorage.getItem('token');
    if(yourAuthToken !=null){
    this.manipulateuser.getContacts(yourAuthToken).subscribe((data:any[])=>{
      console.log(data);
      
      this.contacts = data.map(contact => ({
        name: contact.firstname + ' ' + contact.lastname,
        email: contact.email,
        phoneno: contact.phoneno,
      }));
    });
  }
  else{
    console.log("token not found");
    
  }
  }

}