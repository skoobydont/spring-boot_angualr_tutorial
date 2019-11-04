package com.ajskubak.projectname.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ajskubak.projectname.model.Skill;
import com.ajskubak.projectname.model.TagModel;
import com.ajskubak.projectname.model.UserModel;
import com.ajskubak.projectname.repository.SkillRepository;
import com.ajskubak.projectname.repository.TagRepository;
import com.ajskubak.projectname.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    // need to autowire our repositories for use
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SkillRepository skillRepo;
    @Autowired
    private TagRepository tagRepo;

    /*
     * =============================== 
     * BEGIN USER SERVICE METHODS
     * ===============================
     */
    // add a new user
    public ResponseEntity<?> addUser(UserModel user) {
        // check if user already exists
        ArrayList<UserModel> users = new ArrayList<UserModel>();
        userRepo.findAll().forEach(users::add);
        // auto increment has been difficult so use this to help
        long auto_id = 0;
        // check if fields are blank
        if (user == null || user.getUsername().equals("") || user.getDept().equals("")) {
            return new ResponseEntity<String>("User Fields May Not Be Empty", HttpStatus.NOT_ACCEPTABLE);
        }
        for (UserModel u : users) {
            if (u.getUsername().equals(user.getUsername()) && u.getId() == user.getId()) {
                return new ResponseEntity<String>("User " + user.getUsername() + " already exists",
                        HttpStatus.CONFLICT);
            }
            // auto-increment id based on database
            else if (u.getId() == user.getId() || user.getId() == auto_id) {
                user.setId(u.getId() + 1);
            }
        }
        userRepo.save(user);
        return new ResponseEntity<UserModel>(user, HttpStatus.CREATED);
    }

    // method to get all users from repo
    public ResponseEntity<?> getAllUsers() {
        // check for empty db
        if (userRepo.count() == 0)
            return new ResponseEntity<String>("No users Exist", HttpStatus.NO_CONTENT);
        else {
            ArrayList<UserModel> users = new ArrayList<>();
            userRepo.findAll().forEach(users::add);
            return new ResponseEntity<List<UserModel>>(users, HttpStatus.OK);
        }
    }

    // get user by id
    public ResponseEntity<?> getUser(long id) {
        if (userRepo.existsById(id)) {
            return new ResponseEntity<UserModel>(userRepo.getOne(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User with Id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    // delete a user based on id
    public ResponseEntity<?> deleteUser(long id) {
        // check if user exists
        if (!userRepo.existsById(id)) {
            return new ResponseEntity<String>("User with Id: " + id + " is not found", HttpStatus.NOT_FOUND);
        } else {
            // delete user
            userRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    // delete all users
    public ResponseEntity<?> deleteAllUsers() throws Exception {
        // delete all users
        userRepo.deleteAll();
        // now check if alluser list empty
        List<UserModel> allUsers = new ArrayList<UserModel>();
        userRepo.findAll().forEach(allUsers::add);
        if (allUsers.size() > 0) {
            return new ResponseEntity<String>("Error Deleting Users", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<String>("Users Deleted", HttpStatus.OK);
        }
    }

    // update user by id
    public ResponseEntity<?> updateUserById(UserModel user) throws Exception {
        if (user.getUsername().isEmpty() || user.getDept().isEmpty()) {
            return new ResponseEntity<String>("Cannot Update With Empty Fields", HttpStatus.NO_CONTENT);
        }
        if (!userRepo.existsById(user.getId())) {
            return new ResponseEntity<String>("User with Id: " + user.getId() + " Not Found", HttpStatus.NOT_FOUND);
        }
        userRepo.save(user);
        return new ResponseEntity<UserModel>(user, HttpStatus.OK);
    }

    /*
     * =============================== 
     * END USER SERVICE METHODS
     * ===============================
     */
    /*
     * =============================== 
     * BEGIN SKILL SERVICE METHODS
     * ===============================
     */
    // add skill
    public ResponseEntity<?> addSkilltoUser(Skill skill, long user_id) {
        //check if any fields are empty
        if(skill == null || skill.getSkill().equals("")){
            return new ResponseEntity<String>("Skill may not be null or have empty fields",HttpStatus.NOT_ACCEPTABLE);
        }// grab user from userRepo
        Optional<UserModel> user = userRepo.findById(user_id);
        //instantiate arraylist of skills and populate with skills from user
        ArrayList<Skill> allSkills = new ArrayList<Skill>();
        user.get().getSkills().forEach(allSkills::add);
        // check if given skill already exists in list of user skills
        for(Skill s: allSkills){//if given skill is the same as a skill in user list, refuse duplicate skill entry
            if(skill.getSkill().equals(s.getSkill())){
                return new ResponseEntity<String>("Skill already exists with user: "+user.get().getUsername(),HttpStatus.CONFLICT);
            }
        }//if skill not found within user skill list, add to user list and save user
        UserModel save = user.get();
        save.getSkills().add(skill);
        //skill.getUsers().add(save);
        userRepo.save(save);
        return new ResponseEntity<UserModel>(save,HttpStatus.CREATED);
        
    }
    //get list of every skill
    public ResponseEntity<?> getAllSkills(){
        //check if there are skills
        ArrayList<Skill> allSkills = new ArrayList<Skill>();
        skillRepo.findAll().forEach(allSkills::add);
        if(allSkills.size()==0){
            return new ResponseEntity<String>("No Skills Found",HttpStatus.NOT_FOUND);
        }// else return all skills
        return new ResponseEntity<List<Skill>>(skillRepo.findAll(),HttpStatus.OK);
    }
    //get skills for specific user based on id
    public ResponseEntity<?> getSkillsByUserId(long user_id){
        //check if skill exists in user skill list
        Optional<UserModel> user = userRepo.findById(user_id);
        //create arraylist of skills to check if skills are in it
        ArrayList<Skill> userSkills = new ArrayList<Skill>();
        //loop through user skill list and add to userskills
        user.get().getSkills().forEach(userSkills::add);
        //if the userskills list is smaller than 1 skill, throw 404
        if(userSkills.size()<1){
            return new ResponseEntity<String>("User has no associated skills",HttpStatus.NOT_FOUND);
        } else { //return list of skills based on user id
            return new ResponseEntity<Set<Skill>>(userRepo.getOne(user_id).getSkills(),HttpStatus.OK); 
        } 
    }
    //delete skill by user id
    public ResponseEntity<?> deleteSkillByUserId(long skill_id, long user_id){
        //if the user skill list does not contain skill, 404
        //get user
        Optional<UserModel> user = userRepo.findById(user_id);
        ArrayList<Skill> allSkills = new ArrayList<Skill>();
        //check if skill is in user skill list
        user.get().getSkills().forEach(allSkills::add);
        //get skill in question
        Optional<Skill> deleteThis = skillRepo.findById(skill_id);
        //check if skill exists
        if(!deleteThis.isPresent()){
            return new ResponseEntity<String>("Skill Does Not Exist",HttpStatus.NOT_FOUND);
        }
        if(!allSkills.contains(deleteThis.get())){
            return new ResponseEntity<String>("Skill with id:"+skill_id+" unassociated with user:"+user_id,HttpStatus.CONFLICT);
        } else {// if the user has the skill, remove it from list
            userRepo.getOne(user_id).getSkills().remove(skillRepo.getOne(skill_id));
            //if the skill belongs to no one else, delete skill alltogether
            if(skillRepo.getOne(skill_id).getUsers().isEmpty()){
                skillRepo.delete(skillRepo.getOne(skill_id));
            } //return 200
            userRepo.save(user.get());
            return new ResponseEntity<String>("Skill removed",HttpStatus.OK);
        }
    }
    //delete all skills
    public ResponseEntity<?> deleteAllSkills(){
        //try to delete all skills
        skillRepo.deleteAll();
        //check if all skills deleted
        ArrayList<Skill> allSkills = new ArrayList<Skill>();
        skillRepo.findAll().forEach(allSkills::add);
        if(allSkills.size()>0){
            return new ResponseEntity<String>("Error Deleting Skills",HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<String>("Skills Deleted",HttpStatus.OK);
        }
    }
    //get one skill from user
    public ResponseEntity<?> getOneSkillFromUser(long user_id, long skill_id) {
        //grab user
        Optional<UserModel> user = userRepo.findById(user_id);
        ArrayList<Skill> allSkills = new ArrayList<Skill>();
        //return the one that matches the input skill id
        Optional<Skill> oneSkill = skillRepo.findById(skill_id);
        if(!oneSkill.isPresent()){
            return new ResponseEntity<String>("Skill Does Not Exist",HttpStatus.BAD_REQUEST);
        }
        //grab their skills
        user.get().getSkills().forEach(allSkills::add);
        //if skill doesn't exist, 404
        if(!allSkills.contains(oneSkill.get())){
            return new ResponseEntity<String>("Skill with id: "+skill_id+" was not found with user: "+user_id,HttpStatus.NOT_FOUND);
        }
        //if skill exists, 200
        return new ResponseEntity<Skill>(oneSkill.get(),HttpStatus.OK);
    }
    //update skill 
    public ResponseEntity<?> updateSkill(long skill_id, Skill skill){
        //check if skill exists
        Optional<Skill> exists = skillRepo.findById(skill_id);
        if(!exists.isPresent()){
            //no skill, 404
            return new ResponseEntity<String>("Skill with id: "+skill_id+" not found",HttpStatus.NOT_FOUND);
        }
        //update and save skill
        Skill save = exists.get();
        save.setSkill(skill.getSkill());
        //return updated skill and 200
        skillRepo.save(save);
        return new ResponseEntity<Skill>(save,HttpStatus.OK);
    }
    /* 
    * ===============================
    * END SKILL SERVICE METHODS
    * =============================== 
    */
    /* 
    * ===============================
    * BEGIN TAG SERVICE METHODS
    * =============================== 
    */
    //get all tags
    public ResponseEntity<?> getAllTags(){
        //check if there are any tags
        ArrayList<TagModel> allTags = new ArrayList<TagModel>();
        tagRepo.findAll().forEach(allTags::add);
        //no tags, 404
        if(allTags.size()<1){
            return new ResponseEntity<String>("No Tags Found",HttpStatus.NOT_FOUND);
        }
        //if tags, 200 and list of all tags
        return new ResponseEntity<List<TagModel>>(allTags,HttpStatus.OK);
    }
    //get all tags for specific skill
    public ResponseEntity<?> getAllTagsOfSkill(long skill_id){
        //check if skill has tags
        Optional<Skill> skill = skillRepo.findById(skill_id);
        //no skill, bad request
        if(!skill.isPresent()){
            return new ResponseEntity<String>("Skill with id: "+skill_id+" not found",HttpStatus.BAD_REQUEST);
        }
        //skill with no tags, 404
        if(skill.get().getTags().isEmpty()){
            return new ResponseEntity<String>("Skill "+skill.get().getId()+" has no tags", HttpStatus.NOT_FOUND);
        }
        //else return list of skill tags
        return new ResponseEntity<Set<TagModel>>(skill.get().getTags(),HttpStatus.OK);
    }
    //add tag to skill
    public ResponseEntity<?> addTagToSkill(TagModel tag, long skill_id){
        //check if skill exists
        Optional<Skill> skill = skillRepo.findById(skill_id);
        if(!skill.isPresent()){
            //no skill, bad request
            return new ResponseEntity<String>("Skill with id:"+skill_id+" not found", HttpStatus.BAD_REQUEST);
        }
        //ensure tag is not duplicate in skill already
        for(TagModel t: skill.get().getTags()){
            if(t.getTagDescription().equals(tag.getTagDescription())){
                //if duplicate, conflict
                return new ResponseEntity<String>("Tag "+tag.getId()+", "+tag.getTagDescription()+" already exists with user id:"+skill_id,HttpStatus.CONFLICT);
            }
        }
        //add tag to skill's tag list
        Skill save = skill.get();
        save.getTags().add(tag);
        //save skill & return 200
        skillRepo.save(save);
        return new ResponseEntity<TagModel>(tag,HttpStatus.CREATED);
    }
    //remove tag from skill
    public ResponseEntity<?> removeTagFromSkill(long tag_id, long skill_id){
        //check if skill exists
        Optional<Skill> skill = skillRepo.findById(skill_id);
        if(!skill.isPresent()){
            //no skill, bad request
            return new ResponseEntity<String>("Skill with id: "+skill_id+" was not found",HttpStatus.BAD_REQUEST);
        }
        //check if tag exists
        Optional<TagModel> tag = tagRepo.findById(tag_id);
        if(!tag.isPresent()){
            return new ResponseEntity<>("Tag with id:"+tag_id+" does not exist",HttpStatus.NOT_ACCEPTABLE);
        }
        //check if skill has tag
        if(!skill.get().getTags().contains(tag.get())){
            //no tag, not found
            return new ResponseEntity<String>("id: "+tag.get().getId()+", tag: "+tag.get().getTagDescription()+" not found with skill id:"+skill_id,HttpStatus.NOT_FOUND);
        }
        //remove tag from skill tag list
        skill.get().getTags().remove(tag.get());
        //save skill, return 200
        skillRepo.save(skill.get());
        return new ResponseEntity<Set<TagModel>>(skill.get().getTags(),HttpStatus.OK);
    }
    //update tag
    public ResponseEntity<?> updateTag(TagModel tag, long tag_id){
        //check if tag exists
        Optional<TagModel> exists = tagRepo.findById(tag_id);
        if(!exists.isPresent()){
            //doesn't exist, not found
            return new ResponseEntity<>("Unable to update nonexistent tag:"+tag,HttpStatus.BAD_REQUEST);
        }
        //exists, update
        exists.get().setTagDescription(tag.getTagDescription());
        //save and return 200
        tagRepo.save(exists.get());
        return new ResponseEntity<TagModel>(exists.get(),HttpStatus.OK);
    }
    //delete all tags
    public ResponseEntity<?> deleteAllTags(){
        //delete all tags
        tagRepo.deleteAll();
        //check repo for all tags
        ArrayList<TagModel> allTags = new ArrayList<TagModel>();
        tagRepo.findAll().forEach(allTags::add);
        //if tags still remain, conflict
        if(!allTags.isEmpty()){
            return new ResponseEntity<String>("Error Deleting All Tags",HttpStatus.CONFLICT);
        } else {//else return 200
            return new ResponseEntity<String>("All Tags Deleted",HttpStatus.OK);
        }
        
    }
    /* 
    * ===============================
    * END TAG SERVICE METHODS
    * =============================== 
    */
}