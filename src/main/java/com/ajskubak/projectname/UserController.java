package com.ajskubak.projectname;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserService service;
    //get user based on id
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public @ResponseBody UserModel getUser(@PathVariable("id") int id) throws Exception {
        UserModel user = service.getUser(id);
        return user;
    }
    //get all users
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public @ResponseBody List<UserModel> getAllUsers() throws Exception {
        return service.getAllUsers();
    }
    //delete user based on id
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("id") int id) throws Exception {
        service.deleteUser(id);
    }
    //add new user
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public @ResponseBody UserModel addUser(UserModel user) throws Exception {
        return service.addUser(user);
    }
}