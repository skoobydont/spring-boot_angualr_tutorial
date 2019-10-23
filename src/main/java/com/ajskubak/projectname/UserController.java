package com.ajskubak.projectname;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserServiceImpl service;
    //get user based on id
    @GetMapping(path = {"/user/{id}"})
    public ResponseEntity<?> getUser(@PathVariable("id") long id) throws Exception {
        return service.getUser(id);
    }
    //get all users
    @GetMapping(path = {"/user"})
    public ResponseEntity<?> getAllUsers() throws Exception {
        return service.getAllUsers();
    }
    //delete user based on id
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) throws Exception {
        return service.deleteUser(id);
    }
    //delete all users
    @DeleteMapping(value = "/user")
    public ResponseEntity<?> deleteAllUsers() throws Exception{
        return service.deleteAllUsers();
    }
    //add new user
    @PostMapping(value = "/user")
    public ResponseEntity<?> addUser(@RequestBody UserModel user) throws Exception {
        return service.addUser(user);
    }
    //update user
    @PutMapping(value = "/user")
    public ResponseEntity<?> updateUser(@RequestBody UserModel update) throws Exception {
        return service.updateUserById(update);
    }
}