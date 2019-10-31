package com.ajskubak.projectname.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.ajskubak.projectname.model.Skill;
import com.ajskubak.projectname.model.UserModel;
import com.ajskubak.projectname.repository.SkillRepository;
import com.ajskubak.projectname.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    // need to autowire our repositories for use
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private SkillRepository skillRepo;

    /*
     * =============================== 
     * BEGIN USER SERVICE METHODS
     * ===============================
     */
    // add a new user
    public ResponseEntity<?> addUser(UserModel user) {
        // check if user already exists
        ArrayList<UserModel> users = new ArrayList<UserModel>();
        userRepo.findAll().forEach(users::add);
        // auto increment has been difficult so use this to help
        long auto_id = 0;
        // check if fields are blank
        if (user == null || user.getUsername().equals("") || user.getDept().equals("")) {
            return new ResponseEntity<String>("User Fields May Not Be Empty", HttpStatus.NOT_ACCEPTABLE);
        }
        for (UserModel u : users) {
            if (u.getUsername().equals(user.getUsername()) && u.getId() == user.getId()) {
                return new ResponseEntity<String>("User " + user.getUsername() + " already exists",
                        HttpStatus.CONFLICT);
            }
            // auto-increment id based on database
            else if (u.getId() == user.getId() || user.getId() == auto_id) {
                user.setId(u.getId() + 1);
            }
        }
        userRepo.save(user);
        return new ResponseEntity<UserModel>(user, HttpStatus.CREATED);
    }

    // method to get all users from repo
    public ResponseEntity<?> getAllUsers() {
        // check for empty db
        if (userRepo.count() == 0)
            return new ResponseEntity<String>("No users Exist", HttpStatus.NO_CONTENT);
        else {
            ArrayList<UserModel> users = new ArrayList<>();
            userRepo.findAll().forEach(users::add);
            return new ResponseEntity<List<UserModel>>(users, HttpStatus.OK);
        }
    }

    // get user by id
    public ResponseEntity<?> getUser(long id) {
        if (userRepo.existsById(id)) {
            return new ResponseEntity<UserModel>(userRepo.getOne(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("User with Id: " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    // delete a user based on id
    public ResponseEntity<?> deleteUser(long id) {
        // check if user exists
        if (!userRepo.existsById(id)) {
            return new ResponseEntity<String>("User with Id: " + id + " is not found", HttpStatus.NOT_FOUND);
        } else {
            // delete user
            userRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    // delete all users
    public ResponseEntity<?> deleteAllUsers() throws Exception {
        // delete all users
        userRepo.deleteAll();
        // now check if alluser list empty
        List<UserModel> allUsers = new ArrayList<UserModel>();
        userRepo.findAll().forEach(allUsers::add);
        if (allUsers.size() > 0) {
            return new ResponseEntity<String>("Error Deleting Users", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<String>("Users Deleted", HttpStatus.OK);
        }
    }

    // update user by id
    public ResponseEntity<?> updateUserById(UserModel user) throws Exception {
        if (user.getUsername().isEmpty() || user.getDept().isEmpty()) {
            return new ResponseEntity<String>("Cannot Update With Empty Fields", HttpStatus.NO_CONTENT);
        }
        if (!userRepo.existsById(user.getId())) {
            return new ResponseEntity<String>("User with Id: " + user.getId() + " Not Found", HttpStatus.NOT_FOUND);
        }
        userRepo.save(user);
        return new ResponseEntity<UserModel>(user, HttpStatus.OK);
    }

    /*
     * =============================== 
     * END USER SERVICE METHODS
     * ===============================
     */
    /*
     * =============================== 
     * BEGIN SKILL SERVICE METHODS
     * ===============================
     */
    // add skill
    public ResponseEntity<?> addSkilltoUser(Skill skill, long user_id) {
        //check if any fields are empty
        if(skill == null || skill.getSkill().equals("")){
            return new ResponseEntity<String>("Skill may not be null or have empty fields",HttpStatus.NOT_ACCEPTABLE);
        }// grab user from userRepo
        Optional<UserModel> user = userRepo.findById(user_id);
        //instantiate arraylist of skills and populate with skills from user
        ArrayList<Skill> allSkills = new ArrayList<Skill>();
        user.get().getSkills().forEach(allSkills::add);
        // check if given skill already exists in list of user skills
        for(Skill s: allSkills){//if given skill is the same as a skill in user list, refuse duplicate skill entry
            if(skill.getSkill().equals(s.getSkill())){
                return new ResponseEntity<String>("Skill already exists with user: "+user.get().getUsername(),HttpStatus.CONFLICT);
            }
        }//if skill not found within user skill list, add to user list and save user
        UserModel save = user.get();
        save.getSkills().add(skill);
        //skill.getUsers().add(save);
        userRepo.save(save);
        return new ResponseEntity<UserModel>(save,HttpStatus.CREATED);
        
    }
    //get list of every skill
    public ResponseEntity<?> getAllSkills(){
        //check if there are skills
        ArrayList<Skill> allSkills = new ArrayList<Skill>();
        skillRepo.findAll().forEach(allSkills::add);
        if(allSkills.size()==0){
            return new ResponseEntity<String>("No Skills Found",HttpStatus.NOT_FOUND);
        }// else return all skills
        return new ResponseEntity<List<Skill>>(skillRepo.findAll(),HttpStatus.OK);
    }
    //get skills for specific user based on id
    public ResponseEntity<?> getSkillsByUserId(long user_id){
        //check if skill exists in user skill list
        if(userRepo.getOne(user_id).getSkills().isEmpty()){
            return new ResponseEntity<String>("User has no associated skills",HttpStatus.NOT_FOUND);
        } //return list of skills based on user id
        userRepo.save(userRepo.getOne(user_id));
        return new ResponseEntity<Set<Skill>>(userRepo.getOne(user_id).getSkills(),HttpStatus.OK); 
    }
    //delete skill by user id
    public ResponseEntity<?> deleteSkillByUserId(long skill_id, long user_id){
        //if the user skill list does not contain skill, 404
        if(!userRepo.getOne(user_id).getSkills().contains(skillRepo.getOne(skill_id))){
            return new ResponseEntity<String>("Skill with id:"+skill_id+" unassociated with user:"+user_id,HttpStatus.NOT_FOUND);
        } else {// if the user has the skill, remove it from list
            userRepo.getOne(user_id).getSkills().remove(skillRepo.getOne(skill_id));
            //if the skill belongs to no one else, delete skill alltogether
            if(skillRepo.getOne(skill_id).getUsers().isEmpty()){
                skillRepo.delete(skillRepo.getOne(skill_id));
            } //return 200
            userRepo.save(userRepo.getOne(user_id));
            return new ResponseEntity<String>("Skill removed",HttpStatus.OK);
        }
    }
    //delete all skills
    public ResponseEntity<?> deleteAllSkills(){
        //try to delete all skills
        skillRepo.deleteAll();
        //check if all skills deleted
        ArrayList<Skill> allSkills = new ArrayList<Skill>();
        skillRepo.findAll().forEach(allSkills::add);
        if(allSkills.size()>0){
            return new ResponseEntity<String>("Error Deleting Skills",HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<String>("Skills Deleted",HttpStatus.OK);
        }
    }
    /* 
    * ===============================
    * END SKILL SERVICE METHODS
    * =============================== 
    */
}