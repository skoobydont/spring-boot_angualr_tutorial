import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { UserService } from './user.service';
import { MessageService } from './message.service';

export class User {
  constructor(
    public status: string,
  ) {}
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
/*
  check out this tutorial:
    https://blog.angular-university.io/angular-jwt-authentication/
*/


  constructor(
    private httpClient: HttpClient,
    private userService: UserService,
    private messageService: MessageService,
  ) { }

  //authenticate method
  authenticate(username, password){
    console.log('username:'+username);
    console.log('password:'+password);
    //build authentication headers
    const headers = new HttpHeaders({Authorization: 'Bearer '+ btoa(username + ':' + password)});
    //return get method
    return this.httpClient.post<User>('http:localhost:8080/login', { headers }).pipe(
      map(
        userData => {
          sessionStorage.setItem('username', username);
          let authString = 'Bearer ' + btoa(username + ':' + password);
          sessionStorage.setItem('token',authString);
          return userData;
        }
      )
    );
  }
  //check if user is logged in
  isUserLoggedIn() {
    let user = sessionStorage.getItem('username');
    this.messageService.add('username:'+user);
    console.log(!(user === null));
    return !(user === null);
  }
  //logout method
  logOut() {
    sessionStorage.removeItem('username');
  }
}
