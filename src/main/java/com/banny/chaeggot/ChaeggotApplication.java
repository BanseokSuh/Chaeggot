package com.banny.chaeggot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ChaeggotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChaeggotApplication.class, args);
		// test
	}

}
