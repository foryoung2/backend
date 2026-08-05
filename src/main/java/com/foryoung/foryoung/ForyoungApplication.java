package com.foryoung.foryoung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ForyoungApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForyoungApplication.class, args);
	}

}
