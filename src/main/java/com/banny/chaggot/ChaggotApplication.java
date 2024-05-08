package com.banny.chaggot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ChaggotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaggotApplication.class, args);
		// test
	}

}
