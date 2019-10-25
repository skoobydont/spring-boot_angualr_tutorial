import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { UserService } from '../user.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-delete-user',
  templateUrl: './delete-user.component.html',
  styleUrls: ['./delete-user.component.css']
})
export class DeleteUserComponent implements OnInit {
  //get the user from the url
  @Input() user: User;
  //extract the id from url to user in getUser() method below
  id = +this.route.snapshot.paramMap.get('id');

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private location: Location,
    private router: Router,
    private messageService: MessageService
  ) { }
  ngOnInit() {
    this.getUser();
  }
  
  getUser(): void {
    this.userService.getUser(this.id).subscribe(user => this.user = user);
  }
  goBack(): void {
    this.location.back();
  }
  deleteUser(id: number) {
    //todo: call user service to delete user
    this.messageService.add('delete user: '+id);
    this.userService.deleteUser(id).subscribe();
    this.router.navigate(['/users']);
  }
}
