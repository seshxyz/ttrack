package com.thiscompany.ttrack.enums.converter;

import com.thiscompany.ttrack.enums.TaskState;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StateConverter extends GenericParamConverter<TaskState> {
	
	protected StateConverter() {
		super(TaskState.class);
	}
	
}
