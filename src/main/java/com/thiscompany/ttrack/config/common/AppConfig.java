package com.thiscompany.ttrack.config.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thiscompany.ttrack.config.security.JWT.JwtProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.json.ProblemDetailJacksonMixin;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties({AppConfig.class, JwtProperties.class})
@ConfigurationProperties(prefix = "app")
public class AppConfig {
	
	@Value("${info.application.version}")
	private String version;
	
	@Bean
	OpenAPI swaggerConfig() {
		Contact contact = new Contact();
		contact.setName("seshxyz");
		
		Info info = new Info();
		info.setTitle("2track API");
		info.setDescription("2track API documentation");
		info.setContact(contact);
		info.setVersion(version);
		
		OpenAPI swagger = new OpenAPI();
		swagger.setInfo(info);
		swagger.servers(Collections.emptyList());
		swagger.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
			   .components(new Components().addSecuritySchemes("Bearer Authentication", securityScheme()));
		return swagger;
	}
	
	private SecurityScheme securityScheme() {
		return new SecurityScheme()
				   .type(SecurityScheme.Type.HTTP)
				   .scheme("bearer")
				   .bearerFormat("JWT");
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		objectMapper.addMixIn(ProblemDetail.class, ProblemDetailJacksonMixin.class);
		return objectMapper;
	}
	
	@Bean
	public ExecutorService kafkaExecutorService() {
		return new ThreadPoolExecutor(
			2,
			3,
			5L,
			TimeUnit.SECONDS,
			new SynchronousQueue<>()
		);
	}
}
