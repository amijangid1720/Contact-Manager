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
    path:'navbar1', component:Navbar1Component
  },
  // {
  //   path: 'api/v1/contacts/add',
  //   component: AddComponent,
  //   canActivate: [authGuard],
  // },
  // { path: 'update-contact/:id',component:UpdateContactComponent},
  {path:'',component:HomeComponent,
    children:[
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'update-contact/:id',
        component:UpdateContactComponent
      },
      {
        path:'add',
        component: AddComponent,
      },

      {path:'table',
    component:ContactTableComponent}
    ]
}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
