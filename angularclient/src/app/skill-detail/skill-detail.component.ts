import { Component, OnInit, Input } from '@angular/core';
import { Skill } from '../skill';
import { Location } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { MessageService } from '../message.service';

@Component({
  selector: 'app-skill-detail',
  templateUrl: './skill-detail.component.html',
  styleUrls: ['./skill-detail.component.css']
})
export class SkillDetailComponent implements OnInit {
  @Input() skill: Skill;
  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private location: Location,
    private router: Router,
    private messagingService: MessageService
  ) { }

  ngOnInit() {
    this.getSkill();
  }

  getSkill(): void {
    const user_id = +this.route.snapshot.paramMap.get('user_id');
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    this.userService.getSkill(user_id,skill_id).subscribe(skill => this.skill = skill);
  }
  goBack(): void {
    this.location.back();
  }
}
