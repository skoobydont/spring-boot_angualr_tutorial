package com.ajskubak.projectname.controller;

import com.ajskubak.projectname.model.Skill;
import com.ajskubak.projectname.model.UserModel;
import com.ajskubak.projectname.service.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    /* ===============================
    BEGIN USER ENDPOINTS
    =============================== */
    //get user based on id
    @GetMapping(path = {"/user/{id}"})
    public ResponseEntity<?> getUser(@PathVariable("id") long id) throws Exception {
        return userService.getUser(id);
    }
    //get all users
    @GetMapping(path = {"/user"})
    public ResponseEntity<?> getAllUsers() throws Exception {
        return userService.getAllUsers();
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
    @DeleteMapping(value = "/user/{id}/skill")
    public ResponseEntity<?> deleteSkillByUserId(@PathVariable("id") long user_id, @RequestBody long skill_id) throws Exception{
        return userService.deleteSkillByUserId(skill_id,user_id);
    }
    //delete all skills
    @DeleteMapping(value = "/skills")
    public ResponseEntity<?> deleteAllSkills() throws Exception {
        return userService.deleteAllSkills();
    }
    /* ===============================
    END SKILL ENDPOINTS
    =============================== */
}