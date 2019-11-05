package com.ajskubak.projectname;

import com.ajskubak.projectname.model.Skill;
import com.ajskubak.projectname.model.TagModel;
import com.ajskubak.projectname.model.UserModel;
import com.ajskubak.projectname.repository.UserRepository;
import com.ajskubak.projectname.service.UserServiceImpl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjectnameApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjectnameApplication.class, args);
	}
	//add some dummy data
	@Bean
	CommandLineRunner init(UserServiceImpl userService, UserRepository userRepo){
		return args -> {
			UserModel user2 = new UserModel("Abbie","GD");
			userService.addUser(user2);
			UserModel user3 = new UserModel("Jack","OR");
			userService.addUser(user3);
			userRepo.findAll().forEach(System.out::println);
			Skill skill1 = new Skill("sk1ll");
			Skill skill2 = new Skill("sk2ll");
			TagModel tag1 = new TagModel("Design");
			skill1.getTags().add(tag1);
			user2.getSkills().add(skill1);
			user2.getSkills().add(skill2);
			user3.getSkills().add(skill2);
			userRepo.save(user2);
			userRepo.save(user3);
		};
	}
}
