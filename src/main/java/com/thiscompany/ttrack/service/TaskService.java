package com.thiscompany.ttrack.service;

import com.thiscompany.ttrack.controller.payload.NewTaskRequest;
import com.thiscompany.ttrack.controller.payload.TaskResponse;

public interface TaskService {

    TaskResponse createTask(NewTaskRequest newTaskRequest);

    TaskResponse getTask(Long id);

}
