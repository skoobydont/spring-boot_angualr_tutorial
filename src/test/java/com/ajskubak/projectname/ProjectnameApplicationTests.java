package com.ajskubak.projectname;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.ResultHandler.*;

import java.util.ArrayList;

import javax.transaction.Transactional;

import com.ajskubak.projectname.model.Skill;
import com.ajskubak.projectname.model.TagModel;
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
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProjectnameApplicationTests {
	@Autowired
	UserServiceImpl userService;
	/* auto generated id values for skills are a pain;
	*  some tests use hard url values to access correct skills
	*  as more skill tests are added, be prepared to update other tests ):
	*/
	UserModel user1 = new UserModel("USER1","DEPT1");
	UserModel user2 = new UserModel("USER2","DEPT2");
	UserModel user3 = new UserModel("US3R","D3PT");
	UserModel user4 = new UserModel("","dept");
	UserModel user5 = new UserModel("US4R","");
	UserModel user6 = new UserModel(1,"6S3R","D36T");
	UserModel emptyUser = new UserModel("","");
	Skill skill1 = new Skill("sk1ll");
	Skill skill2 = new Skill("sk2ll");
	TagModel tag1 = new TagModel("tag1");
	TagModel tag2 = new TagModel("tag2");

	//this objmapper will help us write objects as json strings for testing
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	//need to autowire our mockmvc object
	@Autowired
	private MockMvc mock;
	
	@Before public void deleteAllUsers() throws Exception {
		userService.deleteAllUsers();
	}
	/* ==================================================================================
	* BEGIN USER TESTS
	* ===================================================================================
	*/
	//test if we can add user successfully 
	@Transactional
	@Test
	public void addUserSuccessTest() throws Exception {
		this.mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user3))
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
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
		.andExpect(status().isConflict());
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
		.andExpect(status().isOk());
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
		mock.perform(get("/user/"+user1.getId())).andExpect(status().isOk());
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
		mock.perform(delete("/user/"+user1.getId())).andExpect(status().isOk());
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
	//test if update blank field still updates user
	@Transactional
	@Test
	public void updateUserWithBlankUsernameOrDepartmentTest() throws Exception {
		//add a user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to update with blank username
		mock.perform(patch("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user4))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		//try to update with blank dept
		mock.perform(patch("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user5))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	//test if updating user with blank fields returns no content
	@Transactional
	@Test
	public void updateUserWithEmptyFieldsTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());		
		//try to update it with empty fields
		mock.perform(patch("/user")
		.content(OBJECT_MAPPER.writeValueAsString(emptyUser))
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
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
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
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	/* ==================================================================================
	* END USER TESTS
	* ===================================================================================
	*/
	/* ==================================================================================
	* BEGIN SKILL TESTS
	* ===================================================================================
	*/
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
	//test if we can get skill based on user
	@Transactional
	@Test
	public void getSkillByUserSuccessTest() throws Exception {
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
		//grab skills from user1
		mock.perform(get("/user/"+user1.getId())).andExpect(status().isOk());
		//grab skill from user2
		mock.perform(get("/user/"+user2.getId())).andExpect(status().isOk());
	}
	//test if we get error when getting skill from user without skills
	@Transactional
	@Test
	public void getSkillByUserFailureTest() throws Exception {
		mock.perform(delete("/user")).andExpect(status().isOk());
		mock.perform(delete("/skills")).andExpect(status().isOk());
		//add user 1
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//test if user1 has skills
		mock.perform(get("/user/"+user1.getId()+"/skills")).andExpect(status().isNotFound());
	}
	//test if we can delete skill from user
	@Transactional
	@Test
	public void deleteSkillByUserIdSuccessTest() throws Exception {
		mock.perform(delete("/user")).andExpect(status().isOk());
		mock.perform(delete("/skills")).andExpect(status().isOk());
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill to user1
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//validate if user has skills
		mock.perform(get("/user/"+user1.getId()+"/skills")).andExpect(status().isOk());
		//delete skill from user1
		//tried using skill1.getId() but with auto generated annotation, the id of skill1 is 10
		mock.perform(delete("/user/"+user1.getId()+"/skill/10")).andExpect(status().isOk());
		//validate user1 has no skills; noob
		mock.perform(get("/user/"+user1.getId()+"/skills")).andExpect(status().isNotFound());
		//test fails when run in isolation but with mvn verify, it passes
	}
	//test if 404 responded when we try to delete nonexistent skill from user
	@Transactional
	@Test
	public void deleteNonexistentSkillFromUserTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to delete some skill
		mock.perform(delete("/user/"+user1.getId()+"/skill/5")).andExpect(status().isNotFound());
	}
	//test if we get conflict when trying to delete skill unassociated with user
	@Transactional
	@Test
	public void deleteSkillUnassociatedWithUser() throws Exception {
		//add user 1
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add user 2
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill 2 to user 2
		mock.perform(post("/user/2")
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//test if conflict when trying to delete skill 1 from user 1
		mock.perform(delete("/user/1/skill/25")).andExpect(status().isConflict());
		//auto generated id for this skill is 25
	}
	//test if we can get one skill from user
	@Transactional
	@Test
	public void getOneSkillFromUserSuccessTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//test if get one skill returns 200
		mock.perform(get("/user/"+user1.getId()+"/skill/11")).andExpect(status().isOk());
	}
	//test if we get 404 from get one skill of user
	@Transactional
	@Test
	public void getOneSkillFromUserFailureTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add user2
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill to different user
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to access skill from user 2
		mock.perform(get("/user/2/skill/8")).andExpect(status().isNotFound());
	}
	//test if update skill success
	@Transactional
	@Test
	public void updateOneSkillofUserSuccessTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//update skill
		mock.perform(patch("/skill/7")
		.content(OBJECT_MAPPER.writeValueAsString(skill2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		//validate
	}
	//test if update skill failure
	@Transactional
	@Test
	public void updateOneSkillofUserFailureTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to update nonexistent skill
		mock.perform(patch("/skill/7")
		.content(OBJECT_MAPPER.writeValueAsString(skill2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}
	/* ==================================================================================
	* END SKILL TESTS
	* ===================================================================================
	*/
	/* ==================================================================================
	* BEGIN TAG TESTS
	* ===================================================================================
	*/
	//test add tag success
	@Transactional
	@Test
	public void addTagSuccessTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag to skill
		mock.perform(post("/skill/30")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}
	//test if add duplicate tag to skill errors
	@Transactional
	@Test
	public void addDuplicateTagToSkillTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag to skill
		mock.perform(post("/skill/16")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to add same tag again
		mock.perform(post("/skill/16")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
	}
	//test adding tag to nonexistent skill
	@Transactional
	@Test
	public void addTagToNonexistentSkillTest() throws Exception {
		mock.perform(post("/skill/2")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	//test get all tags regardless of skill
	@Transactional
	@Test
	public void getAllTagsRegardlessOfSkillSuccessTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add user2
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill to user1
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag to skill
		mock.perform(post("/skill/14")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill to user2
		mock.perform(post("/user/2")
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag to skill
		mock.perform(post("/skill/15")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//test if get all tags results in 200
		mock.perform(get("/tags")).andExpect(status().isOk());
	}
	//test if get all tags when empty returns 404
	@Transactional
	@Test
	public void getAllTagsNotFoundTest() throws Exception {
		//delete all tags
		mock.perform(delete("/tags")).andExpect(status().isOk());
		//ensure get all returns 404
		mock.perform(get("/tags")).andExpect(status().isNotFound());
	}
	//test if get all tags from one skill
	@Transactional
	@Test
	public void getAllTagsFromSkillSuccessTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add user2
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill to user1
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag to skill
		mock.perform(post("/skill/23")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill to user2
		mock.perform(post("/user/2")
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag to skill
		mock.perform(post("/skill/24")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//get tags for skill1 user1
		mock.perform(get("/skill/23/tags")).andExpect(status().isOk());
		//get tags for skill1 user2
		mock.perform(get("/skill/24/tags")).andExpect(status().isOk());
	}
	//test if get tag from nonexistent skill
	@Transactional
	@Test
	public void getTagsFromNonexistentSkillTest() throws Exception {
		mock.perform(get("/skill/1/tags")).andExpect(status().isBadRequest());
	}
	//test if skill exists but no tags
	@Transactional
	@Test
	public void getTagsFromSkillFailureTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//check if error
		mock.perform(get("/skill/9/tags")).andExpect(status().isNotFound());
	}
	//test if can remove tag from skill
	@Transactional
	@Test
	public void removeTagFromSkillSuccessTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag
		mock.perform(post("/skill/20")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn().getResponse();
		mock.perform(get("/skill/20/tags")).andExpect(status().isOk());
		//remove tag
		mock.perform(delete("/skill/20/tags/6")).andExpect(status().isOk());
		//check if skill has any tags
		mock.perform(get("/skill/20/tags")).andExpect(status().isNotFound());
	}
	//try to remove tag from nonexistent skill
	@Transactional
	@Test
	public void removeTagFromNonexistentSkill() throws Exception {
		//try to add skill to nonexistent skill
		mock.perform(delete("/skill/15/tags/5")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	//try to remove nonexistent tag from skill
	@Transactional
	@Test
	public void removeNonexistentTagFromSkillTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//try to remove nonexistent tag
		mock.perform(delete("/skill/28/tags/5")).andExpect(status().isNotAcceptable());
	}
	//test if remove existing tag from skill that does not have that tag
	@Transactional
	@Test
	public void removeTagFromUnassociatedSkillTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill1
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill2
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag to skill1
		mock.perform(post("/skill/22")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		mock.perform(get("/skill/22/tags")).andExpect(status().isOk());
		//try to remove tag from skill2
		mock.perform(delete("/skill/21/tags/7")).andExpect(status().isNotFound());
	}
	//test if can update existing tag
	@Transactional
	@Test
	public void updateExistingTagTest() throws Exception {
		//add user
		mock.perform(post("/user")
		.content(OBJECT_MAPPER.writeValueAsString(user1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add skill
		mock.perform(post("/user/"+user1.getId())
		.content(OBJECT_MAPPER.writeValueAsString(skill1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//add tag to skill
		mock.perform(post("/skill/13")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
		//check tags
		mock.perform(get("/skill/13/tags")).andExpect(status().isOk());
		//update tag
		mock.perform(patch("/tag/2")
		.content(OBJECT_MAPPER.writeValueAsString(tag2))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	//test updating nonexistent tag
	@Transactional
	@Test
	public void updateNonexistentTagTest() throws Exception {
		//update nonexistent tag
		mock.perform(patch("/tag/1")
		.content(OBJECT_MAPPER.writeValueAsString(tag1))
		.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	/* ==================================================================================
	* END TAG TESTS
	* ===================================================================================
	*/
}