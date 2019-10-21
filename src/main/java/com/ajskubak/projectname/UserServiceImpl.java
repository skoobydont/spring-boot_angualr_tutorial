package com.ajskubak.projectname;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    // need to autowire our repository for use
    @Autowired
    private UserRepository repo;

    // get user by id
    public ResponseEntity<?> getUser(int id) {
        Optional<UserModel> exists = repo.findById(id);
        if(exists.isPresent()){
            return new ResponseEntity<UserModel>(exists.get(),HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User not found",HttpStatus.NOT_FOUND);
        }
    }
 
    // method to get all users from repo
    public ResponseEntity<?> getAllUsers() {
       //check for empty db
       if(repo.count() == 0)
           return new ResponseEntity<String>("No users Exist",HttpStatus.NO_CONTENT);
       else {
           ArrayList<UserModel> users = new ArrayList<>();
           repo.findAll().forEach(users::add);
           return new ResponseEntity<List<UserModel>>(users, HttpStatus.OK);
       }
    }
 
    // delete a user based on id
    public ResponseEntity<?> deleteUser(int id){
        //check if user exists
        if(!repo.existsById(id)){
            return new ResponseEntity<String>("User does not exists",HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<String>("User deleted", HttpStatus.OK);
        }
    }

    // add a new user
    public ResponseEntity<?> addUser(UserModel user){
        //check if user already exists
        ArrayList<UserModel> users = new ArrayList<UserModel>();
        repo.findAll().forEach(users::add);
        for(UserModel u: users){
            if(u.getName().equals(user.getName())){
                return new ResponseEntity<String>("User "+user.getName()+" already exists",HttpStatus.CONFLICT);
            }
        }
        repo.save(user);
        return new ResponseEntity<String>("User "+user.getName()+" added",HttpStatus.CREATED);
    }
    //delete all users
    public ResponseEntity<?> deleteAllUsers() throws Exception {
        //delete all users
        repo.deleteAll();
        //now check if alluser list empty
        List<UserModel> allUsers = new ArrayList<UserModel>();
        repo.findAll().forEach(allUsers::add);
        if(allUsers.size()>0){
            return new ResponseEntity<String>("Error Deleting Users",HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<String>("Users Deleted",HttpStatus.OK);
        }
    }
}