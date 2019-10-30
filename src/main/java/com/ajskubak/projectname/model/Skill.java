package com.ajskubak.projectname.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Skill {
    @Id
    @GeneratedValue
    private long id;
    private String skill;
    //need to add jsonignore or else infinite loop when query skills or users
    @JsonIgnore
    @ManyToMany(mappedBy = "skills")
    private Set<UserModel> users = new HashSet<UserModel>();

    public Skill(){}
    public Skill(String skill){
        // this.id = 1;
        this.skill = skill;
    }
    public String getSkill(){
        return this.skill;
    }
    public void setSkill(String skill){
        this.skill = skill;
    }
    public Long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }
    public Set<UserModel> getUsers(){
        return this.users;
    }
    public void setUsers(Set<UserModel> users){
        this.users = users;
    }
}