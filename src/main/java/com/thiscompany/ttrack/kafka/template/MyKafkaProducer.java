package com.thiscompany.ttrack.kafka.template;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyKafkaProducer<T> {

    private static final Logger log = LogManager.getLogger(MyKafkaProducer.class);
    private final KafkaTemplate<String, T> template;

    public void sendEvent(String topic, T messageObject) {
        Message<T> message = MessageBuilder
                .withPayload(messageObject)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
        template.send(message);
        log.info("Message sent: {}", messageObject);
    }

}
