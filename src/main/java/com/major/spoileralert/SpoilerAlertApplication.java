package com.major.spoileralert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpoilerAlertApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpoilerAlertApplication.class, args);
	}
}