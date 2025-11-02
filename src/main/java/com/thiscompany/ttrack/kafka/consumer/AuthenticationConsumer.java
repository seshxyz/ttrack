package com.thiscompany.ttrack.kafka.consumer;

import com.thiscompany.ttrack.controller.user.dto.UserEntryTimestamp;
import com.thiscompany.ttrack.service.user.post_authentication.PostAuthenticationEventProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationConsumer {
    
    private final PostAuthenticationEventProcessor authenticationProcessor;
    
    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${app.kafka.group-id}", info = "auth-event")
    public void consumeAuthEvent(UserEntryTimestamp info) {
        authenticationProcessor.process(info);
    }

}
