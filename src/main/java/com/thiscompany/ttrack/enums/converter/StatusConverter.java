package com.thiscompany.ttrack.enums.converter;

import com.thiscompany.ttrack.enums.TaskStatus;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter extends GenericParamConverter<TaskStatus> {

    public StatusConverter() {
        super(TaskStatus.class);
    }
}
