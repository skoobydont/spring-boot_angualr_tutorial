package com.ajskubak.projectname.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TagModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String tagDescription;

    //json ignore to prevent infinite loop when querying
    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Set<Skill> skills = new HashSet<Skill>();

    //default constructor
    public TagModel(){}
    //description constructor
    public TagModel(String desc){
        this.tagDescription = desc;
    }
    //getters and setters
    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }
    public String getTagDescription(){
        return this.tagDescription;
    }
    public void setTagDescription(String desc){
        this.tagDescription = desc;
    }
    public Set<Skill> getSkills(){
        return this.skills;
    }
    public void setSkills(Set<Skill> skills){
        this.skills = skills;
    }

}