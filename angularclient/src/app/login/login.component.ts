import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  title = 'login';
  greeting = {'id':'XXX', 'content':'Heyo Worldie'};

  constructor() { }

  ngOnInit() {
  }

}
