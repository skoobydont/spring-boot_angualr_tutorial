package com.ajskubak.projectname;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
     // need to autowire our repository for use
     @Autowired
     private UserRepository repo;
 
     // method to get one user based on id given
     public UserModel getUser(int id) {
         UserModel user = repo.getOne(id);
         return user;
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
     public void deleteUser(int id) {
         repo.deleteById(id);
     }
 
     // add a new user
     public UserModel addUser(UserModel user) {
         UserModel exist = repo.getOne(user.getId());
         if(exist != null){
             return exist;
         }
         repo.save(user);
         return user;
     }
}