package com.ajskubak.projectname.repository;

import com.ajskubak.projectname.model.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel,Long> {
    UserModel findByUsername(String username);
}