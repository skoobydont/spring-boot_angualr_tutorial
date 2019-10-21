package com.ajskubak.projectname;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	@Autowired
	private MockMvc mock;

	
	//TODO: write test for read by id, read all, and delete userModel
	//test if we can add user successfully 
	@Transactional
	@Test
	public void addUserSuccessTest() throws Exception {
		this.mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(new UserModel(12,"username","deptTest")))
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
	}
	//test if duplicate addition returns 409 http status
	@Transactional
	@Test
	public void addUserConflictTest() throws Exception {
		//check if adding user works - 201
		this.mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(new UserModel(1,"John","IT")))
		.contentType(MediaType.APPLICATION_JSON));
		//expect the duplicate error is returned - 409
		this.mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(new UserModel(1,"John","IT")))
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isConflict());
	}
	//test if get all users
	@Transactional
	@Test
	public void getAllUsersTest() throws Exception {
		mock.perform(get("/user").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	//test if no users available 204
	@Transactional
	@Test
	public void getAllUsersFailTest() throws Exception {
		//delete all users
		mock.perform(delete("/user")).andExpect(status().isOk());
		//now check if 204 returned
		mock.perform(get("/user")).andExpect(status().isNoContent());
	}
	public static String asJsonString(final Object obj){
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
