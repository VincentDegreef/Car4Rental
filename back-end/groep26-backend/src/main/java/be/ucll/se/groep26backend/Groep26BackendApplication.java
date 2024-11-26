package be.ucll.se.groep26backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

@SpringBootApplication
// @EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
public class Groep26BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Groep26BackendApplication.class, args);
	}

}
