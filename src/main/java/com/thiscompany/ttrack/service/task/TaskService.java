package com.thiscompany.ttrack.service.task;

import com.thiscompany.ttrack.controller.task.dto.NewTaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskResponse;
import com.thiscompany.ttrack.controller.task.dto.TaskUpdateRequest;
import com.thiscompany.ttrack.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(NewTaskRequest request, User user);

    TaskResponse createDraftTask(NewTaskRequest request, User user);

    TaskResponse findTaskById(String id, String requestUser);

    List<TaskResponse> findAllUserTasks(String requestUser);

    List<TaskResponse> findAllUserActiveTasks(String requestUser);

    TaskResponse updateTask(String id, TaskUpdateRequest request, String requestUser);

    void deleteTask(String id, String requestUser);

    Page<TaskResponse> findTasksByParams(
        String requestUser, String title, String details, String status, String state, String priority, boolean completed,
        Pageable pageable
    );

}
