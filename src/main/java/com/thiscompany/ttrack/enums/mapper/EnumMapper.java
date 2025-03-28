package com.thiscompany.ttrack.enums.mapper;

import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class EnumMapper {

    private final static Map<String, Priority> PRIORITY_MAP =
            Stream.of(Priority.values())
                    .collect(Collectors.toMap(
                            name -> name.name().toLowerCase(),
                            priority -> priority)
                    );

    private final static Map<String, TaskStatus> STATUS_MAP =
            Stream.of(TaskStatus.values())
                    .collect(Collectors.toMap(
                            name -> name.name().toLowerCase(),
                            status -> status)
                    );

    private final static Map<String, TaskState> STATE_MAP =
            Stream.of(TaskState.values())
                    .collect(Collectors.toMap(
                            name -> name.name().toLowerCase(),
                            state -> state)
                    );

    @Named("mapToPriority")
    public static Priority mapToPriority(String input) {
        return input == null ? null : PRIORITY_MAP.get(input.toLowerCase());
    }

    @Named("mapToStatus")
    public static TaskStatus mapToStatus(String input) {
        return input == null ? null : STATUS_MAP.get(input.toLowerCase());
    }

    @Named("mapToState")
    public static TaskState mapToState(String input) {
        return input == null ? null : STATE_MAP.get(input.toLowerCase());
    }

    @Named("mapFromStatus")
    public static String mapFromStatus(TaskStatus input){
        return input == null ? null : input.name().toLowerCase();
    }

    @Named("mapFromPriority")
    public static String mapFromPriority(Priority input){
        return input == null ? null : input.name().toLowerCase();
    }

    @Named("mapFromState")
    public static String mapFromState(TaskState input){
        return input == null ? null : input.name().toLowerCase();
    }

}
