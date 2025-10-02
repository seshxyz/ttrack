package com.thiscompany.ttrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;


@SpringBootApplication
public class ToTrackApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ToTrackApplication.class, args);
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+3:00"));
	}
	
}
