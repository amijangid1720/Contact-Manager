import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule ,HTTP_INTERCEPTORS} from '@angular/common/http';
import { DashboardComponent } from './dashboard/dashboard.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AuthInterceptorService} from './service/auth-interceptor.service'; // Import your interceptor
import {
  GoogleLoginProvider,
  SocialAuthServiceConfig,
  SocialLoginModule,
  GoogleSigninButtonModule,
} from '@abacritt/angularx-social-login';
import { Navbar1Component } from './navbar1/navbar1.component';
import { ContactTableComponent } from './contact-table/contact-table.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AddComponent } from './add/add.component';
import { UpdateContactComponent } from './update-contact/update-contact.component';
import { HomeComponent } from './home/home.component';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

import { PaginatorModule } from 'primeng/paginator';
import { PasswordModule } from 'primeng/password';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { ProfileComponent } from './profile/profile.component';
const config: SocialAuthServiceConfig = {
  providers: [
    {
      id: GoogleLoginProvider.PROVIDER_ID,

      provider: new GoogleLoginProvider(
        '360599613542-j8il63optd440q20iknopkmttkev7lj2.apps.googleusercontent.com'
      ),
    },
    // Add other providers as needed
  ],
};
@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    DashboardComponent,
    Navbar1Component,
    ContactTableComponent,
    AddComponent,
    UpdateContactComponent,
    HomeComponent,
    ProfileComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    SocialLoginModule,
    GoogleSigninButtonModule,
    FontAwesomeModule,
    ToastModule,
    ConfirmDialogModule,
    HttpClientModule,

    PaginatorModule,

    PasswordModule,
    AvatarModule,
    AvatarGroupModule

  ],

  providers: [
    MessageService,
    {
      provide: 'SocialAuthServiceConfig',
      useValue: config,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService, // Add your interceptor here
      multi: true,
    }
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
