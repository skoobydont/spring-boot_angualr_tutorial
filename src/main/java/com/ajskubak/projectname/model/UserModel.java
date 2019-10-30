package com.ajskubak.projectname.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class UserModel {
    @Id
    private long id;
    private String username;
    private String dept;
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_skill",
    joinColumns = @JoinColumn(name="skill_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
    private Set<Skill> skills = new HashSet<Skill>();
    //constructors
    public UserModel(){}
    public UserModel(String username, String dept) {
        this.id = 1;
        this.username = username;
        this.dept = dept;
    }
    public UserModel(long id, String username, String dept){
        this.id = id;
        this.username = username;
        this.dept = dept;
    }
    //getters + setters
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
    public Set<Skill> getSkills(){
        return this.skills;
    }
    public void setSkills(Set<Skill> skills){
        this.skills = skills;
    }
    @Override
    public String toString() {
        return "Id: " + this.id 
        + ", Username: "+this.username
        +", Dept: "+this.dept
        +", Password: "+this.password;
    }
}