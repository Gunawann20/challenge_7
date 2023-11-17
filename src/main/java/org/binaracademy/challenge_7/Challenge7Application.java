package org.binaracademy.challenge_7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Challenge7Application {

	public static void main(String[] args) {
		SpringApplication.run(Challenge7Application.class, args);
	}

}
