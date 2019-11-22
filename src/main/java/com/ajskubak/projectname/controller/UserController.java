package com.ajskubak.projectname.controller;

import java.security.Principal;

import com.ajskubak.projectname.model.Skill;
import com.ajskubak.projectname.model.TagModel;
import com.ajskubak.projectname.model.UserModel;
import com.ajskubak.projectname.repository.UserRepository;
import com.ajskubak.projectname.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPwdEncoder;
    //constructor
    @Autowired
    private UserRepository userRepo;

    /* ===============================
    BEGIN USER ENDPOINTS
    =============================== */
    //get user based on id
    @GetMapping(path = {"/user/{id}"})
    public ResponseEntity<?> getUser(@PathVariable("id") long id) throws Exception {
        return userService.getUser(id);
    }
    //get all users
    @GetMapping(path = {"/users"})
    public ResponseEntity<?> getAllUsers() throws Exception {
        return userService.findAllUsers();
    }
    //delete user based on id
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) throws Exception {
        return userService.deleteUser(id);
    }
    //delete all users
    @DeleteMapping(value = "/user")
    public ResponseEntity<?> deleteAllUsers() throws Exception{
        return userService.deleteAllUsers();
    }
    //add new user
    @PostMapping(value = "/user")
    public ResponseEntity<?> addUser(@RequestBody UserModel user) throws Exception {
        return userService.addUser(user);
    }
    /* ===============================
    BEGIN AUTHENTICATION ENDPOINTS
    =============================== */
    //sign up user
    @PostMapping(value = "/sign-up")
    public void signUp(@RequestBody UserModel user){
        user.setPassword(bCryptPwdEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
    /* ===============================
    END AUTHENTICATION ENDPOINTS
    =============================== */

    //update user
    @PatchMapping(value = "/user")
    public ResponseEntity<?> updateUser(@RequestBody UserModel update) throws Exception {
        return userService.updateUserById(update);
    }
    /* ===============================
    END USER ENDPOINTS
    =============================== */
    /* ===============================
    BEGIN SKILL ENDPOINTS
    =============================== */
    //get skills
    @GetMapping(path = "/skills")
    public ResponseEntity<?> getAllSkills() throws Exception {
        return userService.getAllSkills();
    }
    //get skills by user id
    @GetMapping(path = "/user/{id}/skills")
    public ResponseEntity<?> getSkillsByUserId(@PathVariable("id") long id) throws Exception {
        return userService.getSkillsByUserId(id);
    }
    //add skill to user
    @PostMapping(value = "/user/{id}")
    public ResponseEntity<?> addSkilltoUser(@RequestBody Skill skill,@PathVariable("id") long user_id) throws Exception {
        return userService.addSkilltoUser(skill, user_id);
    }
    //delete skill by user id
    @DeleteMapping(value = "/user/{user_id}/skill/{skill_id}")
    public ResponseEntity<?> deleteSkillByUserId(@PathVariable("user_id") long user_id, @PathVariable("skill_id") long skill_id) throws Exception{
        return userService.deleteSkillByUserId(skill_id,user_id);
    }
    //delete all skills
    @DeleteMapping(value = "/skills")
    public ResponseEntity<?> deleteAllSkills() throws Exception {
        return userService.deleteAllSkills();
    }
    //get one skill from user
    @GetMapping(value = "/user/{user_id}/skill/{skill_id}")
    public ResponseEntity<?> getSkillFromUser(@PathVariable("user_id") long user_id, @PathVariable("skill_id") long skill_id){
        return userService.getOneSkillFromUser(user_id, skill_id);
    }
    //update skill
    @PatchMapping(value = "/skill/{skill_id}")
    public ResponseEntity<?> updateSkill(@PathVariable("skill_id") long skill_id, @RequestBody Skill skill){
        return userService.updateSkill(skill_id, skill);
    }
    /* ===============================
    END SKILL ENDPOINTS
    =============================== */
    /* ===============================
    BEGIN TAG ENDPOINTS
    =============================== */
    //get all tags regardless of skill
    @GetMapping(value = "/tags")
    public ResponseEntity<?> getAllTags(){
        return userService.getAllTags();
    }
    //get all tags for skill
    @GetMapping(value = "/skill/{skill_id}/tags")
    public ResponseEntity<?> getAllTagsForSkill(@PathVariable("skill_id") long skill_id){
        return userService.getAllTagsOfSkill(skill_id);
    }
    //get one tag from skill
    @GetMapping(value = "/skill/{skill_id}/tag/{tag_id}")
    public ResponseEntity<?> getOneTagFromSkill(@PathVariable("skill_id") long skill_id, @PathVariable("tag_id") long tag_id){
        return userService.getOneTagFromSkill(skill_id, tag_id);
    }
    //add tag to skill
    @PostMapping(value = "/skill/{skill_id}")
    public ResponseEntity<?> addTagToSkill(@PathVariable("skill_id") long skill_id, @RequestBody TagModel tag){
        return userService.addTagToSkill(skill_id, tag);
    }
    //remove tag from skill
    @DeleteMapping(value = "/skill/{skill_id}/tags/{tag_id}")
    public ResponseEntity<?> removeTagFromSkill(@PathVariable("skill_id") long skill_id, @PathVariable("tag_id") long tag_id){
        return userService.removeTagFromSkill(tag_id, skill_id);
    }
    //update tag
    @PatchMapping(value = "/tag/{tag_id}")
    public ResponseEntity<?> updateTag(@RequestBody TagModel tag, @PathVariable("tag_id") long tag_id){
        return userService.updateTag(tag, tag_id);
    }
    //delete all tags
    @DeleteMapping(value ="/tags")
    public ResponseEntity<?> deleteAllTags() {
        return userService.deleteAllTags();
    }
    /* ===============================
    END TAG ENDPOINTS
    =============================== */

}