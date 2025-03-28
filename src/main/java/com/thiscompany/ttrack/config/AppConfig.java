package com.thiscompany.ttrack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    @Value("${app.version}")
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

        return new OpenAPI().info(info).servers(List.of());
    }

}
