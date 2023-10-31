import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { authGuard } from './auth.guard';
import { Navbar1Component } from './navbar1/navbar1.component';
import { AddComponent } from './add/add.component';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'signup',
    component: RegisterComponent,
  },
  {
    path: 'api/v1/dashboard',
    component: DashboardComponent,
    canActivate: [authGuard],
  },
  {
    path:'navbar', component:Navbar1Component
  },
  {
    path: 'api/v1/contacts/add',
    component: AddComponent,
    canActivate: [authGuard],
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}