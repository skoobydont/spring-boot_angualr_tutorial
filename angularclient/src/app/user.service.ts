import { Injectable } from '@angular/core';

import { User } from './user';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient, private messageService: MessageService) { }
  private url = 'http://localhost:8080/user';
  newUser: User;
  getUsers(): Observable<User[]>{
    this.messageService.add('UserService: fetched all users');
    return this.http.get<User[]>(this.url);
  }
  getUser(id: number): Observable<User> {
    //todo send message _after_ fetching user
    this.messageService.add('UserService: fetched user id='+id);
    return this.http.get<User>(this.url+'/'+id);
  }
  updateUser(user: User) {
    this.messageService.add('Updated User '+user.id);
    return this.http.patch(this.url,user);
  }
  addUser(user: User) {
    this.messageService.add('Added User: '+user.id);
    return this.http.post(this.url,user);
  }
  deleteUser(id: number) {
    this.messageService.add('User with id: '+id+' deleted');
    return this.http.delete(this.url+'/'+id);
  }
}
