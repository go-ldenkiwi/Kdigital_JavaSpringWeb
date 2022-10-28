package com.joongbu.WebSNS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class WebSnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSnsApplication.class, args);
		
	}

}
