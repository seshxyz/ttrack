package com.thiscompany.ttrack.service.user.post_authentication;

import com.thiscompany.ttrack.controller.user.dto.UserEntryTimestamp;
import com.thiscompany.ttrack.core.interfaces.ActionProcessor;
import com.thiscompany.ttrack.core.interfaces.BufferOperator;
import com.thiscompany.ttrack.core.interfaces.ScheduledDecider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostAuthenticationEventProcessor implements ActionProcessor<UserEntryTimestamp> {
	
	private final BufferOperator<UserEntryTimestamp, List<UserEntryTimestamp>> eventBuffer;
	private final ScheduledDecider<UserEntryTimestamp> writeDecider;
	
	@Override
	public void process(UserEntryTimestamp info) {
		boolean executionAllowed = eventBuffer.addToBuffer(info);
		if(executionAllowed){
			writeDecider.tryExecute(info);
		}
	}
	
}
