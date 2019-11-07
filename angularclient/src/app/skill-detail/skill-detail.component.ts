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
  @Input() skill: Skill;
  @Input() allTags: Tag[];
  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private location: Location,
    private router: Router,
    private messagingService: MessageService
  ) { }
  updateSkillForm = new FormGroup({
    desc: new FormControl('')
  });

  ngOnInit() {
    this.getSkill();
    this.getSkillTags();
  }

  getSkill(): void {
    const user_id = +this.route.snapshot.paramMap.get('user_id');
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    this.userService.getSkill(user_id,skill_id).subscribe(skill => this.skill = skill);
  }
  goBack(): void {
    this.location.back();
  }
  getSkillTags(): void {
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    this.messagingService.add('skill detail trying to get tags for skill:'+skill_id);
    this.userService.getSkillTags(skill_id).subscribe(tag => this.allTags = tag);
  }
  updateSkill(){
    let update: Skill = {
      id: this.skill.id,
      skill: this.updateSkillForm.controls.desc.value
    };
    this.messagingService.add('Skill id:'+update.id+' has been updated to '+update.skill);
    this.userService.updateSkill(update).subscribe();
    const user_id = +this.route.snapshot.paramMap.get('user_id');
    this.router.navigate(['/detail/'+user_id]);
  }
}
