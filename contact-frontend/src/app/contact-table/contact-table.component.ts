import { Component } from '@angular/core';
import { faUserPen,faTrashCan,faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-contact-table',
  templateUrl: './contact-table.component.html',
  styleUrls: ['./contact-table.component.scss']
})
export class ContactTableComponent {
  faUserPen = faUserPen;
  faTrashCan = faTrashCan;
  faArrowRightFromBracket = faArrowRightFromBracket;
}
