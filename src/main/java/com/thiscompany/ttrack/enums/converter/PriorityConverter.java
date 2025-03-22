package com.thiscompany.ttrack.enums.converter;

import com.thiscompany.ttrack.enums.Priority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PriorityConverter implements AttributeConverter<Priority, String> {

    @Override
    public String convertToDatabaseColumn(Priority attribute) {
        for (Priority priority : Priority.values()) {
            if (attribute.equals(priority)) {
                return priority.getName();
            }
        }
        throw new IllegalArgumentException("Unable to set defined priority");
    }

    @Override
    public Priority convertToEntityAttribute(String dbData) {
        for (Priority priority : Priority.values()) {
            if (priority.getName().equalsIgnoreCase(dbData)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unable to set defined priority");
    }

}
