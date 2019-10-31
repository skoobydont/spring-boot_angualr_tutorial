import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { UserService } from '../user.service';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from '../message.service';
import { Skill } from '../skill';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() user: User;
  @Input() allSkills: Skill[];
  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private location: Location,
    private router: Router,
    private messageService: MessageService
  ) { }
  updateForm = new FormGroup({
    username: new FormControl(''),
    dept: new FormControl(''),
  });

  ngOnInit() {
    this.getUser();
    this.getUserSkills();
  }
  getUser(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.userService.getUser(id).subscribe(user=> this.user = user);
  }
  goBack(): void {
    this.location.back();
  }
  updateUser() {
    //todo: call service to update user
    let update: User = {
      id: this.user.id,
      username: this.updateForm.controls.username.value,
      dept: this.updateForm.controls.dept.value,
      password: this.user.password
    };
    this.messageService.add(update.username+' updated');
    this.userService.updateUser(update).subscribe();
    this.router.navigate(['/users']);
  }
  getUserSkills(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.userService.getUserSkills(id).subscribe(skill => this.allSkills = skill);
  }
}
