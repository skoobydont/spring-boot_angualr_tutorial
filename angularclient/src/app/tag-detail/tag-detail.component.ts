import { Component, OnInit, Input } from '@angular/core';
import { Tag } from '../tag';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { UserService } from '../service/user.service';
import { MessageService } from '../service/message.service';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
  selector: 'app-tag-detail',
  templateUrl: './tag-detail.component.html',
  styleUrls: ['./tag-detail.component.css']
})
export class TagDetailComponent implements OnInit {
  // obj to hold selected tag
  @Input() tag: Tag;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private location: Location,
    private messageService: MessageService,
    private router: Router
  ) { }

  ngOnInit() {
    this.getTag();
  }
  //form to hold update tag info
  updateTagForm = new FormGroup({
    id: new FormControl(''),
    tagDescription: new FormControl('')
  });
  //get tag based on skill id in url
  getTag(): void {
    //grab skill id from url
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    // grab tag id from url
    const tag_id = +this.route.snapshot.paramMap.get('tag_id');
    // call user service to get specific tag for specific skill
    this.userService.getTag(skill_id,tag_id).subscribe(result => this.tag = result);
  }
  //go back method
  goBack() {
    this.location.back();
  }
  //update tag description
  updateTag() {
    //placeholder obj to hold update form inputs
    let updateTag = {
      id: this.tag.id,
      tagDescription: this.updateTagForm.controls.tagDescription.value
    };// add message
    this.messageService.add('updating tag id:'+this.tag.id+' to: '+updateTag.tagDescription);
    // pass update tag obj to service update method + subscribe to result
    this.userService.updateTag(updateTag).subscribe();
    //grab user id from url
    const user_id = +this.route.snapshot.paramMap.get('user_id');
    //get skill id from url for redirect
    const skill_id = +this.route.snapshot.paramMap.get('skill_id');
    //navigate user to skill detail
    this.router.navigate(['/detail/' + user_id + '/skill/' + skill_id]);
  }

}
