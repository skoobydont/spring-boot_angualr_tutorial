package com.ajskubak.projectname;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    // need to autowire our repository for use
    @Autowired
    private UserRepository repo;

    // method to get one user based on id given
    public UserModel getUser(int id) {
        UserModel user = repo.getOne(id);
        return user;
    }

    // method to get all users from repo
    public List<UserModel> getAllUsers() {
        List<UserModel> userList = new ArrayList<UserModel>();
        repo.findAll().forEach(userList::add);
        return userList;
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