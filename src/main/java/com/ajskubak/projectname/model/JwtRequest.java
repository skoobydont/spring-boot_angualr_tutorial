package com.ajskubak.projectname.model;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;

    //need default constructor for json parsing
    public JwtRequest() {}

    //username password constructor
    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
    //getters and setters
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}