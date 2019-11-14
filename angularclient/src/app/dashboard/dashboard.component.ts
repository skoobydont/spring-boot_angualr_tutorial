import { Component, OnInit } from '@angular/core';
import { User } from '../user';
import { UserService } from '../service/user.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  users: User[] = [];
  title = 'Authentication';
  greeting = {};

  constructor(private userService: UserService, private http: HttpClient) {
    http.get('user').subscribe(data => this.greeting = data);
  }
  //authentication method
  authenticated() {
    return this.userService.authenticated;
  }

  ngOnInit() {
    this.getUsers();
  }
  getUsers(): void {
    this.userService.getUsers().subscribe(users => this.users = users.slice(0,5));
  }

}
