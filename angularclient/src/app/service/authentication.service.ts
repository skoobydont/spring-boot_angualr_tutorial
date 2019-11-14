import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';

export class User {
  constructor(
    public status: string,
  ) {}
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(
    private httpClient: HttpClient
  ) { }

  //authenticate method
  authenticate(username, password){
    console.log(username);
    console.log(password);
    //build authentication headers
    const headers = new HttpHeaders({Authorization: 'Basic '+ btoa(username + ':' + password)});
    //return get method
    return this.httpClient.get<User>('http:localhost:8080/users/validateLogin', { headers }).pipe(
      map(
        userData => {
          sessionStorage.setItem('username', username);
          let authString = 'Basic ' + btoa(username + ':' + password);
          sessionStorage.setItem('basicauth',authString);
          return userData;
        }
      )
    );
  }
  //check if user is logged in
  isUserLoggedIn() {
    let user = sessionStorage.getItem('username');
    console.log(!(user === null));
    return !(user === null);
  }
  //logout method
  logOut() {
    sessionStorage.removeItem('username');
  }
}
