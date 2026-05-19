package com.mathquest.mathquest_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class cdMathquestBackendApplication {
	static void main(String[] args) {
		SpringApplication.run(MathquestBackendApplication.class, args);
	}
}
