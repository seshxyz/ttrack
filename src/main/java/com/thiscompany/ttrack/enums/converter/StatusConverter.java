package com.thiscompany.ttrack.enums.converter;

import com.thiscompany.ttrack.enums.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<TaskStatus, String> {

    @Override
    public String convertToDatabaseColumn(TaskStatus attribute) {
        for (TaskStatus status : TaskStatus.values()) {
            if (attribute.equals(status)) {
                return status.getName();
            }
        }
        throw new IllegalArgumentException("Unable to set defined status");
    }

    @Override
    public TaskStatus convertToEntityAttribute(String dbData) {
        for (TaskStatus status : TaskStatus.values()) {
            if (status.getName().equalsIgnoreCase(dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unable to set defined status");
    }
}
