package com.ajskubak.projectname;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.ajskubak.projectname.model.Skill;
import com.ajskubak.projectname.model.UserModel;
import com.ajskubak.projectname.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectnameApplicationTests {
	@Autowired
	UserServiceImpl userService;

	UserModel user1 = new UserModel("USER1","DEPT1");
	UserModel user2 = new UserModel("USER2","DEPT2");
	UserModel user3 = new UserModel("US3R","D3PT");
	UserModel user4 = new UserModel("","dept");
	UserModel user5 = new UserModel("US4R","");
	UserModel user6 = new UserModel(1,"6S3R","D36T");
	Skill skill1 = new Skill("sk1ll");
	Skill skill2 = new Skill("sk2ll");

	//this objmapper will help us write objects as json strings for testing
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	//need to autowire our mockmvc object
	@Autowired
	private MockMvc mock;
	
	@Before public void deleteAllUsers() throws Exception {
		userService.deleteAllUsers();
	}
	
	//test if we can add user successfully 
	@Transactional
	@Test
	public void addUserSuccessTest() throws Exception {
		this.mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user3))
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andDo(print());
	}
	//test if duplicate addition returns 409 http status
	@Transactional
	@Test
	public void addUserConflictTest() throws Exception {
		//check if adding user works - 201
		this.mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON));
		//expect the duplicate error is returned - 409
		this.mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict())
		.andDo(print());
	}
	//test if get all users
	@Transactional
	@Test
	public void getAllUsersTest() throws Exception {
		//add some users
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user3))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//get all users
		mock.perform(get("/user").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andDo(print());
	}
	//test if no users available - 204
	@Transactional
	@Test
	public void getAllUsersFailTest() throws Exception {
		//delete all users
		mock.perform(delete("/user")).andExpect(status().isOk());
		//now check if 204 returned
		mock.perform(get("/user")).andExpect(status().isNoContent());
	}

	//test if get user by id
	@Transactional
	@Test
	public void getUserByIdSuccessTest() throws Exception {
		//delete all users
		mock.perform(delete("/user")).andExpect(status().isOk());
		//now check if 204 returned
		mock.perform(get("/user")).andExpect(status().isNoContent());
		//add a user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
		//check allusers
		mock.perform(get("/user")).andExpect(status().isOk());
		//now check user by id
		mock.perform(get("/user/"+user1.getId())).andExpect(status().isOk())
		.andDo(print());
	}

	//test if get user by id and not found - 404
	@Transactional
	@Test
	public void getUserByIdFailureTest() throws Exception {
		//delete all users
		mock.perform(delete("/user")).andExpect(status().isOk());
		//now try to find a nonexistent user - should 404
		mock.perform(get("/user/15")).andExpect(status().isNotFound());
	}

	//test if delete user by id success
	@Transactional
	@Test
	public void deleteUserByIdSucessTest() throws Exception {
		mock.perform(post("/user").content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		mock.perform(delete("/user/"+user1.getId())).andExpect(status().isOk()).andDo(print());
	}

	//test if delete user by id failure - not found 404
	@Transactional
	@Test
	public void deleteUserByIdFailureTest() throws Exception {
		mock.perform(delete("/user/15")).andExpect(status().isNotFound());
	}
	//test if delete all users success
	@Transactional
	@Test
	public void deleteAllUsersSuccessTest() throws Exception {
		//add a user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//delete users
		mock.perform(delete("/user")).andExpect(status().isOk());
		//ensure empty users by trying to get all and erroring
		mock.perform(get("/user")).andExpect(status().isNoContent());
	}
	//test if update blank user sends no content 204
	@Transactional
	@Test
	public void updateUserWithBlankFieldsTest() throws Exception {
		//add a user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to update with blank username
		mock.perform(patch("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user4))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		//try to update with blank dept
		mock.perform(patch("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user5))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}
	//test if update nonexistent user returns 404 not found
	@Transactional
	@Test
	public void updateNonexistentUserTest() throws Exception {
		//delete all users
		mock.perform(delete("/user")).andExpect(status().isOk());
		//try to update nonexistent user
		mock.perform(patch("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user3))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound())
		.andDo(print());
	}

	//test if update user is successful
	@Transactional
	@Test
	public void updateUserSucessTest() throws Exception {
		//delete all users
		//mock.perform(delete("/user")).andExpect(status().isOk());
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//validate user added
		String url = "/user/"+user1.getId();
		mock.perform(get(url)).andExpect(status().isOk());
		//update user
		mock.perform(patch("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user6))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andDo(print());
	}

	//test if we can add a skill to user
	@Transactional
	@Test
	public void addSkillstoUserSuccessTest() throws Exception {
		//add new user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//now add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//second skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//check if user has skill in skills attribute
		mock.perform(get("/user/"+user1.getId()+"/skills")).andExpect(status().isOk());
		//add skills to array list and assure the array is not null
		ArrayList<Skill> userSkills = new ArrayList<Skill>();
		user1.getSkills().forEach(userSkills::add);
		assertNotNull(userSkills);
	}
	//test if we add empty skill to user
	@Transactional
	@Test
	public void addEmptySkilltoUserTest() throws Exception {
		//add a user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//now add blank skill to user
		Skill blank = new Skill("");
		//should send back not acceptable
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(blank))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotAcceptable());
	}
	//test if we add duplicate skill to user
	@Transactional
	@Test
	public void addDuplicateSkilltoUserTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to add exact skill again
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
	}
	//test if we can add and then receive a list of skills (regardless of user association)
	@Transactional
	@Test
	public void retrieveAllSkillsRegardlessOfUserTest() throws Exception {
		//add user 1
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add user 2
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill 1 to user 1
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill 2 to user 1
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill 2 to user 2
		mock.perform(post("/user/2")
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to retrieve all skills
		mock.perform(get("/skills")).andExpect(status().isOk());
	}
	//test if retrieving skills before any are added results in 404
	@Transactional
	@Test
	public void retrieveAllSkillsBeforeAddingAnyTest() throws Exception {
		//delete all skills first
		mock.perform(delete("/skills")).andExpect(status().isOk());
		//check if no skills are found
		mock.perform(get("/skills")).andExpect(status().isNotFound());
	}
	
}