package com.ajskubak.projectname;

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
		};
	}
}
