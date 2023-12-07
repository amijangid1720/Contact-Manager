import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { authGuard } from './auth.guard';
import { Navbar1Component } from './navbar1/navbar1.component';
import { AddComponent } from './add/add.component';
import { UpdateContactComponent } from './update-contact/update-contact.component';
import { HomeComponent } from './home/home.component';
import { ContactTableComponent } from './contact-table/contact-table.component';
import { ProfileComponent } from './profile/profile.component';
import { UserDetailsFormComponent } from './user-details-form/user-details-form.component';
import { FavoriteComponent } from './favorite/favorite.component';
import { CategoriesComponent } from './categories/categories.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'signup',
    component: RegisterComponent,
  },
  // {
  //   path: 'api/v1/dashboard',
  //   component: DashboardComponent,
  //   canActivate: [authGuard],
  // },
  {
    path: 'navbar1',
    component: Navbar1Component,
  },
  {
    path:'userdetails' , component:UserDetailsFormComponent,
    canActivate:[authGuard,]
  },
  // {
  //   path: 'api/v1/contacts/add',
  //   component: AddComponent,
  //   canActivate: [authGuard],
  // },
  // { path: 'update-contact/:id',component:UpdateContactComponent},
  {
    path: '',
    component: HomeComponent,
    children: [
      
      {
        path: 'dashboard',
        component: DashboardComponent,
        canActivate: [authGuard],
      },
      
      {
        path: 'update-contact/:id',
        component: UpdateContactComponent,
        canActivate: [authGuard],
      },
      {
        path: 'add',
        component: AddComponent,
        canActivate: [authGuard],
      },

      {
        path: 'table',
        component: ContactTableComponent,
        canActivate: [authGuard],
      },
      {
        path: 'profile',
        component: ProfileComponent,
        canActivate: [authGuard],
      },
      {
        path:'favorite',
        component:FavoriteComponent,
        canActivate:[authGuard],
        children:[
          {
            path: 'update-contact/:id',
            component: UpdateContactComponent,
            canActivate: [authGuard],
          },
        ]
      }
      ,{
        path: 'categories/:category', // <-- :category is a route parameter
        component: CategoriesComponent,
        canActivate: [authGuard],
      },
    ],
  },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
