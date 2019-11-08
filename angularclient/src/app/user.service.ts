import { Injectable } from '@angular/core';

import { User } from './user';
import { Skill } from './skill';
import { Observable, of } from 'rxjs';
import { MessageService } from './message.service';
import { HttpClient } from '@angular/common/http';
import { Tag } from './tag';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private http: HttpClient, private messageService: MessageService) { }
  private userUrl = 'http://localhost:8080/user';
  private skillUrl = 'http://localhost:8080/skill';
  newUser: User;
  // BEGIN USER METHODS
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
  }// END USER METHODS
  // BEGIN SKILL METHODS
  getAllSkills(): Observable<Skill[]>{
    this.messageService.add('Got all skills');
    return this.http.get<Skill[]>(this.skillUrl+'s/');
  }
  getUserSkills(id: number): Observable<Skill[]>{
    this.messageService.add('Get the skills for user: '+id);
    return this.http.get<Skill[]>(this.userUrl+'/'+id+'/skills');
  }
  getSkill(user_id: number, skill_id: number): Observable<Skill> {
    this.messageService.add('fetch skill: '+skill_id+' from user'+user_id);
    return this.http.get<Skill>(this.userUrl+'/'+user_id+'/skill/'+skill_id);
  }
  addSkill(user_id: number, newSkill: Skill){
    this.messageService.add('Added Skill '+newSkill.skill);
    return this.http.post(this.userUrl+'/'+user_id,newSkill);
  }
  updateSkill(skill: Skill) {
    this.messageService.add('Updated Skill:'+skill.id+' to : '+skill.skill);
    return this.http.patch(this.skillUrl+'/'+skill.id,skill);
  }
  deleteSkillFromUser(skill: Skill, user_id: number){
    this.messageService.add('Delete skill:'+skill.skill+' from user:'+user_id);
    return this.http.delete(this.userUrl+'/'+user_id+'/skill/'+skill.id);
  }
  //END SKILL METHODS
  // BEGIN TAG METHODS
  getSkillTags(skill_id: number): Observable<Tag[]> {
    this.messageService.add('fetch tags for skill:'+skill_id);
    return this.http.get<Tag[]>(this.skillUrl+'/'+skill_id+'/tags');
  }
  getTag(skill_id: number, tag_id: number): Observable<Tag> {
    this.messageService.add('fetch tag:'+tag_id);
    return this.http.get<Tag>(this.skillUrl+'/'+skill_id+'/tag/'+tag_id);
  }
  // add new tag to skill
  addTag(skill_id: number, newTag: Tag) {
    // add message
    this.messageService.add('add new tag:' + newTag.tagDescription + ' to skill:' + skill_id);
    // call api and return result
    return this.http.post<Tag>(this.skillUrl + '/' + skill_id, newTag);
  }
  // delete tag from skill
  deleteTagFromSkill(skill_id: number, tag: Tag) {
    // add message
    this.messageService.add('Delete tag id: ' + tag.id);
    // call api and return result
    return this.http.delete(this.skillUrl + '/' + skill_id + '/tags/' + tag.id);
  }
}
