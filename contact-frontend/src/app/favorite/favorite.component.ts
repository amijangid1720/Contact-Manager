import { Component } from '@angular/core';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { Router } from '@angular/router';
import { TooltipModule } from 'primeng/tooltip';
// import { UpdateContactComponent } from '../update-contact/update-contact.component';
import {
  faPen,
faEnvelope,
faPhone,
faMinus,
faUserSlash,
faHeart
} from '@fortawesome/free-solid-svg-icons';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.scss'],
})
export class FavoriteComponent {
  contacts: any[] = [];
  faEnvelope=faEnvelope;
  faPen=faPen;
  faUserSlash=faUserSlash;
  faHeart = faHeart;
  faPhone=faPhone;
  faMinus=faMinus;
  totalRecords!: number;
  editingContactId: number | null = null;
 
  constructor(
    private manipulateuser: ManipulateUserService,
    private router: Router,
    private messageService:MessageService
  ) {}

  ngOnInit(): void {
    this.loadFavorite();
  }

  loadFavorite() {
    const userid = localStorage.getItem('user_id');
    if (userid !== null) {
      const userId = parseInt(userid, 10);
      this.manipulateuser.getFavorite(userId).subscribe((data) => {
        this.contacts = data;
        console.log(data);
     
        this.totalRecords = data.length;
        console.log(this.totalRecords);
        
        
      });
    }
  }
  capitalizeFirstLetter(name: string): string {
    return name.charAt(0).toUpperCase() + name.slice(1);
  }


  toggleUpdate(contactId: number) {
    if (this.editingContactId === contactId) {
      // If the user is already editing, close the update page
      this.router.navigate(['favorite']);
      this.editingContactId = null;
    } else {
      // If the user is not editing, navigate to the update page
      this.router.navigate(['favorite/update-contact', contactId]);
      this.editingContactId = contactId;
    }
  }


  removeFromFavorites(contactId: number) {
    // Call the service method to remove the contact
    this.manipulateuser.removeFromFavorites(contactId).subscribe(() => {
      console.log("removed from fav");
      this.messageService.add({
        severity: 'warn',
        summary: 'Deleted',
        detail: 'Favorite contact removed',
      });
      // After successful removal, reload the favorite contacts
      setTimeout(()=>{
        this.loadFavorite();
      },200)
      
    });

}
}
