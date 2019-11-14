import { Component, OnInit, Input } from '@angular/core';
import { User } from '../user';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { UserService } from '../service/user.service';
import { FormGroup, FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from '../service/message.service';
import { Skill } from '../skill';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit {
  @Input() user: User;
  @Input() allSkills: Skill[];
  addNewSkill: boolean;
  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private location: Location,
    private router: Router,
    private messageService: MessageService
  ) { // initialize show new skill row to false
    this.addNewSkill = false;
    }
  // form to hold update information regarding user
  updateForm = new FormGroup({
    username: new FormControl(''),
    dept: new FormControl(''),
  });
  // form to hold new skill data
  newSkillForm = new FormGroup({
    id: new FormControl(''),
    skill: new FormControl('')
  });
  // when component initializes, run these methods
  ngOnInit() {
    this.getUser();
    this.getUserSkills();
  }
  // get user based on id in url
  getUser(): void {
    // grab id from url
    const id = +this.route.snapshot.paramMap.get('id');
    // get user based on id
    this.userService.getUser(id).subscribe(user=> this.user = user);
  }
  // go back one screen
  goBack(): void {
    this.location.back();
  }
  // update user from form inputs
  updateUser() {
    // grab fields from update user form
    let update: User = {
      id: this.user.id,
      username: this.updateForm.controls.username.value,
      dept: this.updateForm.controls.dept.value,
      password: this.user.password
    }; // display message
    this.messageService.add(update.username+' updated');
    // call user service to patch user
    this.userService.updateUser(update).subscribe();
    // navigate user to updated list of all users
    this.router.navigate(['/users']);
  }
  // get all skills associated with user
  getUserSkills(): void {
    // grab user id from url
    const id = +this.route.snapshot.paramMap.get('id');
    // get associated skills from user service
    this.userService.getUserSkills(id).subscribe(skill => this.allSkills = skill);
  }
  // method to show add skill row when clicked
  showAddSkill() {
    // add message
    this.messageService.add('show me the add skill form');
    // toggle add new skill boolean to toggle display of new skill form
    this.addNewSkill = !this.addNewSkill;
  }
  // delete skill from user
  deleteSkill(skill: Skill) {
    // grab user id from url
    const user_id = +this.route.snapshot.paramMap.get('id');
    // confirm skill delete with user
    confirm('Confirm Delete Skill: '+skill.skill+'?');
    // use user service to delete specific skill from specific user
    this.userService.deleteSkillFromUser(skill,user_id).subscribe();
    // nagivate user back to user detail component
    this.router.navigate(['/detail/'+user_id]);
    // call on user service to get updated list of all skills associated with user
    this.userService.getUserSkills(user_id).subscribe(skill => this.allSkills = skill);
  }
  // add new skill to user
  addSkill() {// grab form values to post new skill
    let newSkill: Skill = {
      id: this.newSkillForm.controls.id.value,
      skill: this.newSkillForm.controls.skill.value
    }; // build request
    // grab user id from url
    const user_id = +this.route.snapshot.paramMap.get('id');
    // post new skill to user service
    this.userService.addSkill(user_id,newSkill).subscribe();
    // navigate user back to detail page and update list of skills
    this.router.navigate(['/detail/'+user_id]);
    // render new list of skills
    this.userService.getUserSkills(user_id).subscribe(skill => this.allSkills = skill);
    // toggle view of add skill row
    this.addNewSkill = false;
    // reset value of add skill form to blank
    this.newSkillForm.controls.skill.setValue('');
  }
}
