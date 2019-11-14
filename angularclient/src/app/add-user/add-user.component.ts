import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { UserService } from '../service/user.service';
import { User } from '../user';
import { FormGroup, FormControl } from '@angular/forms';
import { MessageService } from '../service/message.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {
  addUserForm = new FormGroup({
    id: new FormControl(''),
    username: new FormControl(''),
    dept: new FormControl(''),
    password: new FormControl(''),
  });
  constructor(
    private location: Location, 
    private userService: UserService,
    private messageService: MessageService,
    private router: Router  
    ) { }
    
    ngOnInit() {
    }
    
addUser(){
  //take user data from form and post to service
  let user:User = {
    id: this.addUserForm.controls.id.value,
    username: this.addUserForm.controls.username.value,
    dept: this.addUserForm.controls.dept.value,
    password: this.addUserForm.controls.password.value
  };
  this.messageService.add(user.username+' was added!');
  //use instance of data access service to call add user
  this.userService.addUser(user).subscribe(result => this.userService.getUsers());
  this.router.navigate(['/users']);
  }
  goBack(): void {
    this.location.back();
  }
}
