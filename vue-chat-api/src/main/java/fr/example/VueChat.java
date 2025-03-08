package fr.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VueChat {
	
	public static void main(String[] args) {
		System.setProperty("org.jooq.no-tips", "true");
		System.setProperty("org.jooq.no-logo", "true");
		SpringApplication.run(VueChat.class, args);
	}

}
