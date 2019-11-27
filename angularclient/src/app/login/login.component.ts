import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthenticationService } from '../service/authentication.service';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  title = 'login';
  greeting = { 
    id:'XXX',
    content:'Heyo Worldie'
  };

  loginForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

  constructor(
    private userService: UserService,
    private http: HttpClient,
    private router: Router,
    private authService: AuthenticationService
    ) { }

  ngOnInit() {
  }

  login() {
    this.authService.authenticate(this.loginForm.controls.username.value,this.loginForm.controls.password.value).subscribe();
    this.router.navigateByUrl('/');
    return false;
  }
}
