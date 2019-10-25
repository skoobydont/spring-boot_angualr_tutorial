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
    
    // add a new user
    public ResponseEntity<?> addUser(UserModel user){
        //check if user already exists
        ArrayList<UserModel> users = new ArrayList<UserModel>();
        repo.findAll().forEach(users::add);
        //auto increment has been difficult so use this to help
        long auto_id = 0;
        //check if fields are blank
        if(user == null || user.getUsername().equals("") || user.getDept().equals("")){
            return new ResponseEntity<String>("User Fields May Not Be Empty",HttpStatus.NOT_ACCEPTABLE);
        }
        for(UserModel u: users){
            if(u.getUsername().equals(user.getUsername()) && u.getId()==user.getId()){
                return new ResponseEntity<String>("User "+user.getUsername()+" already exists",HttpStatus.CONFLICT);
            }
            //auto-increment id based on database
            else if(u.getId() == user.getId() || user.getId() == auto_id ){
                user.setId(u.getId()+1);
            }
        }
        repo.save(user);
        System.out.println(user.toString());
        return new ResponseEntity<UserModel>(user,HttpStatus.CREATED);
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

    // get user by id
    public ResponseEntity<?> getUser(long id) {
        if(repo.existsById(id)){
            return new ResponseEntity<UserModel>(repo.getOne(id),HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User with Id: "+id+" not found",HttpStatus.NOT_FOUND);
        }
    } 
 
    // delete a user based on id
    public ResponseEntity<?> deleteUser(long id){
        //check if user exists
        if(!repo.existsById(id)){
            return new ResponseEntity<String>("User with Id: "+id+" is not found",HttpStatus.NOT_FOUND);
        } else {
            //delete user
            repo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
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
    //update user by id
    public ResponseEntity<?> updateUserById(UserModel user) throws Exception {
        if(user.getUsername().isEmpty() || user.getDept().isEmpty()){
            return new ResponseEntity<String>("Cannot Update With Empty Fields",HttpStatus.NO_CONTENT);
        }
        if(!repo.existsById(user.getId())){
            return new ResponseEntity<String>("User with Id: "+user.getId()+" Not Found",HttpStatus.NOT_FOUND);
        }
        repo.save(user);
        return new ResponseEntity<UserModel>(user,HttpStatus.OK);        
    }
}