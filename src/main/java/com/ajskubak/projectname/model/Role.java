package com.ajskubak.projectname.model;

import javax.persistence.*;

@Entity
public class Role {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    private String description;
    //get / setters
    public Long getId() {
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getRoleName(){
        return roleName;
    }
    public void setRoleName(String name){
        this.roleName = name;
    }
    public String getRoleDescription(){
        return description;
    }
    public void setRoleDescription(String desc){
        this.description = desc;
    }
}