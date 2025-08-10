package com.thiscompany.ttrack.service.task;

import com.thiscompany.ttrack.controller.task.dto.NewTaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskResponse;
import com.thiscompany.ttrack.controller.task.dto.TaskUpdateRequest;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(NewTaskRequest request);

    TaskResponse createDraftTask(NewTaskRequest request);

    TaskResponse findTaskById(TaskRequest request, String requestUser);

    List<TaskResponse> findAllUserTasks(String requestUser);

    List<TaskResponse> findAllUserActiveTasks(String requestUser);

    TaskResponse updateTask(String id, TaskUpdateRequest request, String requestUser);

    void deleteTask(String id, String requestUser);

}
