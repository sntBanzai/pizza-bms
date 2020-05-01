package pl.malyszko.jerzy.pizzabms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Jerzy Mayszko
 *
 */
@SpringBootApplication
public class PizzaBMSApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
        SpringApplication.run(PizzaBMSApplication.class, args);
    }
	
}
