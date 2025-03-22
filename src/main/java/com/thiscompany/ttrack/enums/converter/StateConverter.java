package com.thiscompany.ttrack.enums.converter;

import com.thiscompany.ttrack.enums.TaskState;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StateConverter implements AttributeConverter<TaskState, String> {

    @Override
    public String convertToDatabaseColumn(TaskState attribute) {
        for (TaskState state : TaskState.values()) {
            if (attribute.equals(state)) {
                return state.getName();
            }
        }
        throw new IllegalArgumentException("Unable to set defined state");
    }

    @Override
    public TaskState convertToEntityAttribute(String dbData) {
        for (TaskState state : TaskState.values()) {
            if (state.getName().equalsIgnoreCase(dbData)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unable to set defined state");
    }

}
