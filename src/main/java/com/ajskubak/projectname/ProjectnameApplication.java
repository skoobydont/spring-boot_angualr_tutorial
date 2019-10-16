package com.ajskubak.projectname;

import java.util.stream.Stream;

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
	CommandLineRunner init(UserRepository userRepo){
		return args -> {
			Stream.of("John","Jane","Jill","Abbie","Ryan").forEach(name -> {
				UserModel user = new UserModel(name,"IT");
				userRepo.save(user);
			});
			userRepo.findAll().forEach(System.out::println);
		};
	}
}
