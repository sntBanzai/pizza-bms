package pl.malyszko.jerzy.pizzabms;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author Jerzy Mayszko
 *
 */
@SpringBootApplication
public class PizzaBMSApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PizzaBMSApplication.class, args);
	}

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customizeJackson() {
		return new Jackson2ObjectMapperBuilderCustomizer() {

			@Override
			public void customize(
					Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
				jacksonObjectMapperBuilder
						.featuresToEnable(ALLOW_UNQUOTED_FIELD_NAMES);
			}
		};
	}

}
