package com.thiscompany.ttrack.service.task.mapper;

import com.thiscompany.ttrack.controller.task.dto.NewTaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskResponse;
import com.thiscompany.ttrack.controller.task.dto.TaskUpdateRequest;
import com.thiscompany.ttrack.enums.mapper.MapperUtils;
import com.thiscompany.ttrack.model.Task;
import org.mapstruct.*;

@Mapper(
	unmappedTargetPolicy = ReportingPolicy.IGNORE,
	componentModel = "spring",
	nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	uses = MapperUtils.class
)
public interface TaskMapper {
	
	@Mappings({
		@Mapping(source = "priority", target = "priority", qualifiedByName = "mapToPriority")
	})
	Task requestToEntity(NewTaskRequest request);
	
	@Mappings({
		@Mapping(source = "priority", target = "priority", qualifiedByName = "mapFromPriority"),
		@Mapping(source = "state", target = "state", qualifiedByName = "mapFromState"),
		@Mapping(source = "isCompleted", target = "isCompleted")
	})
	TaskResponse entityToResponse(Task task);
	
	@Mapping(source = "priority", target = "priority", qualifiedByName = "mapToPriority")
	void patchEntity(TaskUpdateRequest request, @MappingTarget Task task);
	
}
