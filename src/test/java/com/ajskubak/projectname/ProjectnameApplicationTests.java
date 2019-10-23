package com.ajskubak.projectname;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

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
		mock.perform(put("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user4))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		//try to update with blank dept
		mock.perform(put("/user")
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
		mock.perform(put("/user")
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
		mock.perform(put("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user6))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andDo(print());
	}

	public static String asJsonString(final Object obj){
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
