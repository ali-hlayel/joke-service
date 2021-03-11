package com.neuraljam.jokeservice;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JokeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JokeServiceApplication.class, args);
	}
	@Bean
	public GroupedOpenApi customOpenAPI() {
		return GroupedOpenApi.builder()
				.setGroup("latest")
				.pathsToMatch("/latest/**")
				.build();

	}
}
