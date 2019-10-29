package com.ajskubak.projectname.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Skill {
    @Id
    @GeneratedValue
    private long id;
    private String skill;
    @ManyToMany(mappedBy = "skills")
    private List<UserModel> users;

    public Skill(){}
    public Skill(String skill){
        // this.id = 1;
        this.skill = skill;
        this.users = new ArrayList<UserModel>();
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
    public List<UserModel> getUsers(){
        return this.users;
    }
    public Boolean addUser(UserModel user){
        return this.users.add(user);
    }
    public Boolean removeUser(UserModel remove){
        return this.users.remove(remove);
    }
}