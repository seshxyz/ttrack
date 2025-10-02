package com.thiscompany.ttrack.enums.converter;

import com.thiscompany.ttrack.enums.Priority;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public final class PriorityConverter extends GenericParamConverter<Priority> {
	
	public PriorityConverter() {
		super(Priority.class);
	}
	
}
