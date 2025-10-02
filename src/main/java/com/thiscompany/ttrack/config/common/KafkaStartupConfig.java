package com.thiscompany.ttrack.config.common;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaStartupConfig {
	
	@Bean
	public NewTopic createTopic() {
		return new NewTopic("auth-event", 3, (short) 3);
	}
	
}
