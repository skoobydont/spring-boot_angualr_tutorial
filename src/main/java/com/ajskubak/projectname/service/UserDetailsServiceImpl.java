package com.ajskubak.projectname.service;

import java.util.ArrayList;
import java.util.List;

import com.ajskubak.projectname.model.UserModel;
import com.ajskubak.projectname.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //get user from repo
        UserModel user = userRepo.findByUsername(username);
        //if null, throw uname not found exception
        if(user == null){
            throw new UsernameNotFoundException(String.format("Username %s doesn't exist",username));
        }
        //declare and instantiate list of authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        //get user roles list and add new role to list
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        //instantiate new userdetails obj
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        //return userdetails obj
        return userDetails;
    }
    
}