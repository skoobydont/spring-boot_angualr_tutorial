package com.ajskubak.projectname.service;

import com.ajskubak.projectname.model.UserModel;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserModel findByUsername(String username);

    ResponseEntity<?> findAllUsers();


}