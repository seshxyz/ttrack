package com.thiscompany.ttrack.enums.converter;

import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import org.mapstruct.Named;

import java.util.Map;

public class EnumMapper {

    private final static Map<String, Priority> PRIORITY_MAP = Map.of(
            "low", Priority.LOW,
            "normal", Priority.NORMAL,
            "high", Priority.HIGH
    );

    private final static Map<String, TaskStatus> STATUS_MAP = Map.of(
            "draft", TaskStatus.DRAFT,
            "in progress", TaskStatus.IN_PROGRESS
    );

    private final static Map<String, TaskState> STATE_MAP = Map.of(
            "initial", TaskState.INITIAL,
            "active", TaskState.ACTIVE,
            "expired", TaskState.EXPIRED
    );

    @Named("mapToPriority")
    public static Priority mapToPriority(String input) {
        if (input == null) return null;
        return PRIORITY_MAP.get(input.toLowerCase());
    }

    @Named("mapToStatus")
    public static TaskStatus mapToStatus(String input) {
        if (input == null) return null;
        return STATUS_MAP.get(input.toLowerCase());
    }

    @Named("mapToState")
    public static TaskState mapToState(String input) {
        if (input == null) return null;
        return STATE_MAP.get(input.toLowerCase());
    }

    @Named("mapFromStatus")
    public static String mapFromStatus(TaskStatus input){
        return input.getName();
    }

    @Named("mapFromPriority")
    public static String mapFromPriority(Priority input){
        return input.getName();
    }

    @Named("mapFromState")
    public static String mapFromState(TaskState input){
        return input.getName();
    }

}
