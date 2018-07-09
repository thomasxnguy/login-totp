package com.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.auth")
public class SpringProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);
	}
}
