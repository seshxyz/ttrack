package com.thiscompany.ttrack.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaGenericProducer<T> {
	
	private static final Logger LOG = LoggerFactory.getLogger(KafkaGenericProducer.class);
	
	private final KafkaTemplate<String, T> template;
	
	public void sendEvent(String topic, T messageObject) {
		Message<T> message = MessageBuilder
								 .withPayload(messageObject)
								 .setHeader(KafkaHeaders.TOPIC, topic)
								 .build();
		template.send(message);
		LOG.debug("Message sent: {}", messageObject);
	}
	
}
