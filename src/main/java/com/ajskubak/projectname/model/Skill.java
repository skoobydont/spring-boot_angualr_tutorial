package com.ajskubak.projectname.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String skill;
    //need to add jsonignore or else infinite loop when query skills or users
    @JsonIgnore
    @ManyToMany(mappedBy = "skills")
    private Set<UserModel> users = new HashSet<UserModel>();
    //owner of mtm relationship with tags
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "skill_tag",
    joinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"))
    private Set<TagModel> tags = new HashSet<TagModel>();

    public Skill(){}
    public Skill(String skill){
        // this.id = 0;
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
    public Set<TagModel> getTags(){
        return this.tags;
    }
    public void setTags(Set<TagModel> tags){
        this.tags = tags;
    }
}