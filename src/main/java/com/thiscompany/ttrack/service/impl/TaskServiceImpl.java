package com.thiscompany.ttrack.service.impl;

import com.thiscompany.ttrack.TaskRepository;
import com.thiscompany.ttrack.controller.payload.NewTaskRequest;
import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskStatus;
import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    TaskRepository taskRepo;

    @Transactional
    @Override
    public TaskResponse createTask(NewTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.valueOf(request.status()));
        if(Objects.isNull(request.status())) {
            task.setStatus(TaskStatus.IN_PROGRESS);
        }
        else task.setStatus(TaskStatus.valueOf(request.status()));
        task.setPriority(Priority.NORMAL);
        taskRepo.save(task);
        return new TaskResponse(
                task.getId(), task.getTitle(),
                task.getDescription(), task.getPriority().name(),
                task.getStatus().name()
        );
    }

    @Override
    public TaskResponse getTask(Long id) {
        return taskRepo.findTaskById(id)
                .orElseThrow(()-> new RuntimeException("Task not found"));
    }
}
