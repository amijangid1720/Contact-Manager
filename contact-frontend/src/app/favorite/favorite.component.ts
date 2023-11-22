import { Component } from '@angular/core';
import { ManipulateUserService } from '../service/manipulate-user.service';
import { Router } from '@angular/router';
// import { UpdateContactComponent } from '../update-contact/update-contact.component';
import {
  faPen,
faEnvelope,
faPhone,
faMinus
} from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.scss'],
})
export class FavoriteComponent {
  contacts: any[] = [];
  faEnvelope=faEnvelope;
  faPen=faPen;
  faPhone=faPhone;
  faMinus=faMinus;
  editingContactId: number | null = null;
  constructor(
    private manipulateuser: ManipulateUserService,
    private router: Router
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
  // Add any additional methods or event handlers as needed
}
