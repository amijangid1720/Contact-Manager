import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { environment } from '../environment';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  providers: [HttpClient],
})
export class RegisterComponent {
  firstname: string = '';
  lastname: string = '';
  email: string = '';
  password: string = '';
  phoneno: string = '';
  gender: string = '';
  address: string = '';

  constructor(private http: HttpClient,
    private messageService: MessageService,) {}
  register() {
    console.log('hiiiiii');

    let bodyData = {
      "firstname": this.firstname,
      "lastname": this.lastname,
      "email": this.email,
      "address": this.address,
      "password": this.password,
      "phoneno": this.phoneno,
      "gender": this.gender,
    };
    this.http
      .post(`${environment.apiUrl}auth/register`, bodyData, {
        responseType: 'text',
      })
      .subscribe((resultData: any) => {
        console.log(resultData);
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'Registration Successful ! ',
        });
      });
  }
}
