import { Component } from '@angular/core';

@Component({
  selector: 'app-favorite',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.scss']
})
export class FavoriteComponent{
  favoriteContacts: any[] = [];
  loading: boolean = false;

  constructor() {}

  ngOnInit(): void {
   
  }


  // Add any additional methods or event handlers as needed
}
