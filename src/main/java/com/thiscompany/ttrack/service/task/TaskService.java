package com.thiscompany.ttrack.service.task;

import com.thiscompany.ttrack.controller.payload.NewTaskRequest;
import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.controller.payload.TaskUpdateRequest;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(NewTaskRequest request);

    TaskResponse createDraftTask(NewTaskRequest request);

    TaskResponse getTask(Long id);

    List<TaskResponse> getAllTask();

    TaskResponse updateTask(Long id, TaskUpdateRequest request);

    void deleteTask(Long id);

}
