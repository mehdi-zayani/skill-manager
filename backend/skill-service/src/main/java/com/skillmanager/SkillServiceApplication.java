package com.skillmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Skill Manager application.
 */
@SpringBootApplication
public class SkillServiceApplication {

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(SkillServiceApplication.class, args);
	}
}