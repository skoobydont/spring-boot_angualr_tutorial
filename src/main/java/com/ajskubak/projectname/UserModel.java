package com.ajskubak.projectname;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class UserModel {
    @Id
    private long id;
    private String username;
    private String dept;
    private String password;

    public UserModel(){}

    public UserModel(String username, String dept) {
        this.id = 1;
        this.username = username;
        this.dept = dept;
    }
    public UserModel(int id, String username, String dept){
        this.id = id;
        this.username = username;
        this.dept = dept;
    }

    public long getId() {
        return id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDept(){
        return dept;
    }
    public void setDept(String dept){
        this.dept = dept;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String toString() {
        return "Id: " + this.id + ", Username: "+this.username+", Dept: "+this.dept+", Password: "+this.password;
    }
}