import { Component, OnInit, Input } from '@angular/core';
import { Skill } from '../skill';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { MessageService } from '../message.service';
import { Tag } from '../tag';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-skill-detail',
  templateUrl: './skill-detail.component.html',
  styleUrls: ['./skill-detail.component.css']
})
export class SkillDetailComponent implements OnInit {
  // obj to hold selected skill
  @Input() skill: Skill;
  // obj for list of tags of skill
  @Input() allTags: Tag[];
  // boolean to toggle add skill display
  showAddSkill: boolean;
  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private location: Location,
    private router: Router,
    private messagingService: MessageService
  ) {// instantiate show add skill row to false
      this.showAddSkill = false;
   }
  // name and attributes of update skill form
  updateSkillForm = new FormGroup({
    id: new FormControl(''),
    description: new FormControl('')
  });
  // name + attributes of new tag form
  addTagForm = new FormGroup({
    id: new FormControl(''),
    tagDescription: new FormControl('')
  });
  // get skill and tags on init
  ngOnInit() {
    this.getSkill();
    this.getSkillTags();
  }
  // get the skill based on user id in url
  getSkill(): void {
    // grab user id from url
    const user_id = +this.route.snapshot.paramMap.get('user_id');
    // grab skill id from url
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    // call user service to get the specific skill for specific user
    this.userService.getSkill(user_id, skill_id).subscribe(skill => this.skill = skill);
  }
  // method to go back one component
  goBack(): void {
    this.location.back();
  }
  // method to get all tags on the skill
  getSkillTags(): void {
    // grab skill id from url
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    // add message to msg service
    this.messagingService.add('skill detail trying to get tags for skill:'+skill_id);
    // call user service to get list of tags per skill id
    this.userService.getSkillTags(skill_id).subscribe(tag => this.allTags = tag);
  }
  // method to update specific skill
  updateSkill() {
    // placeholder obj created with update form values
    let update: Skill = {
      id: this.skill.id,
      skill: this.updateSkillForm.controls.desc.value
    }; // add message
    this.messagingService.add('Skill id:' + update.id + ' has been updated to '+update.skill);
    // pass update skill obj to service update method + subscribe to result
    this.userService.updateSkill(update).subscribe();
    // grab user id from url
    const user_id = +this.route.snapshot.paramMap.get('user_id');
    // navigate user to user detail page w/ updated skills list
    this.router.navigate(['/detail/' + user_id]);
  }
  // click + button to toggle add tag form display
  showAddTag() {
    // toggle new tag bool to its opposite
    this.showAddSkill = !this.showAddSkill;
  }
  // method to add tag to skill
  addTag() {
    // use form values to populate placeholder obj
    let newTag: Tag = {
      id: this.addTagForm.controls.id.value,
      tagDescription: this.addTagForm.controls.tagDescription.value
    }; // build post request
    // grap skill id from url
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    // grab user id from url
    const user_id = +this.route.snapshot.paramMap.get('user_id');
    // post new tag to user service and subscribe to result
    this.userService.addTag(skill_id, newTag).subscribe();
    // navigate user to skill detail component w/ updated list of tags
    this.router.navigate(['/detail/' + user_id + '/skill/' + skill_id]);
    // render new list of tags
    this.userService.getSkillTags(skill_id).subscribe(tag => this.allTags = tag);
    // toggle view of add tag form
    this.showAddSkill = false;
    // reset value of add tag form to blank
    this.addTagForm.controls.tagDescription.setValue('');
  }
  // delete tag from skill
  deleteTag(tag: Tag) { // first grab user id from url
    const user_id = +this.route.snapshot.paramMap.get('user_id');
    // grab skill id from url
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    // confirm tag delete
    confirm('Confirm Delete tag: ' + tag.tagDescription + '?');
    // call user service to delete specific tag from specific skill
    this.userService.deleteTagFromSkill(skill_id, tag).subscribe();
    // navigate user to skill detail component
    this.router.navigate(['/detail/' + user_id + '/skill/' + skill_id]);
    // call user service to get updated list of tags
    this.userService.getSkillTags(skill_id).subscribe(updatedTag => this.allTags = updatedTag);
  }
}
