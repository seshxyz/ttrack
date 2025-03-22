package com.thiscompany.ttrack.service.task.mapper;

import com.thiscompany.ttrack.controller.payload.NewTaskRequest;
import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.controller.payload.TaskUpdateRequest;
import com.thiscompany.ttrack.enums.converter.EnumMapper;
import com.thiscompany.ttrack.model.Task;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = EnumMapper.class
)
public interface TaskMapper {

    @Mappings({
            @Mapping(source = "status", target = "status", qualifiedByName = "mapToStatus"),
            @Mapping(source = "priority", target = "priority", qualifiedByName = "mapToPriority")
    })
    Task requestToEntity(NewTaskRequest request);

    @Mappings({
            @Mapping(source = "status", target = "status", qualifiedByName = "mapFromStatus"),
            @Mapping(source = "priority", target = "priority", qualifiedByName = "mapFromPriority"),
            @Mapping(source = "state", target = "state", qualifiedByName = "mapFromState")
    })
    TaskResponse entityToResponse(Task task);

    @Mapping(source = "priority", target = "priority", qualifiedByName = "mapToPriority")
    void patchEntity(TaskUpdateRequest request, @MappingTarget Task task);

}
