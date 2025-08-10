package com.thiscompany.ttrack;

import com.thiscompany.ttrack.config.AppConfig;
import com.thiscompany.ttrack.config.JWT.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({AppConfig.class, JwtProperties.class})
public class ToTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(ToTrackApplication.class, args);
    }

}
