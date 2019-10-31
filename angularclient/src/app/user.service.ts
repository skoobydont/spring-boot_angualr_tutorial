import { Injectable } from '@angular/core';

import { User } from './user';
import { Skill } from './skill';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient, private messageService: MessageService) { }
  private userUrl = 'http://localhost:8080/user';
  private skillUrl = 'http://localhost:8080/skills';
  newUser: User;
  getUsers(): Observable<User[]>{
    this.messageService.add('UserService: fetched all users');
    return this.http.get<User[]>(this.userUrl);
  }
  getUser(id: number): Observable<User> {
    //todo send message _after_ fetching user
    this.messageService.add('UserService: fetched user id='+id);
    return this.http.get<User>(this.userUrl+'/'+id);
  }
  updateUser(user: User) {
    this.messageService.add('Updated User '+user.id);
    return this.http.patch(this.userUrl,user);
  }
  addUser(user: User) {
    this.messageService.add('Added User: '+user.id);
    return this.http.post(this.userUrl,user);
  }
  deleteUser(id: number) {
    this.messageService.add('User with id: '+id+' deleted');
    return this.http.delete(this.userUrl+'/'+id);
  }
  getAllSkills(): Observable<Skill[]>{
    this.messageService.add('Got all skills');
    return this.http.get<Skill[]>(this.skillUrl);
  }
  getUserSkills(id: number): Observable<Skill[]>{
    this.messageService.add('Get the skills for user: '+id);
    this.messageService.add(this.userUrl+'/'+id+'/skills');
    return this.http.get<Skill[]>(this.userUrl+'/'+id+'/skills');
  }
}
