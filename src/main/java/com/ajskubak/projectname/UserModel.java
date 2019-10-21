package com.ajskubak.projectname;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String username;
    private String dept;
    private String password;

    public UserModel(){}

    public UserModel(String username, String dept) {
        this.username = username;
        this.dept = dept;
    }
    public UserModel(int id, String username, String dept){
        this.userId = id;
        this.username = username;
        this.dept = dept;
    }

    public int getId() {
        return userId;
    }
    public String getName() {
        return username;
    }
    public void setName(String username) {
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
        return "Id: " + this.userId + ", Username: "+this.username+", Dept: "+this.dept+", Password: "+this.password;
    }
}