package com.joongbu.WebSNS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"/src/main/java/com.joongbu.WebSNS.mapper"})
public class WebSnsApplication {



	public static void main(String[] args) {
		SpringApplication.run(WebSnsApplication.class, args);
		
	}

}
