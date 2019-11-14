import { Component, OnInit } from '@angular/core';
import { USERS } from '../mock-users';
import { User } from '../user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  allUsers: User[];

  getUsers(): void{
    this.userService.getUsers().subscribe(users => this.allUsers = users);
  }

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.getUsers();
  }

}
