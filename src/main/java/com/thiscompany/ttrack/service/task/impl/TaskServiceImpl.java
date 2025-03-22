package com.thiscompany.ttrack.service.task.impl;

import com.thiscompany.ttrack.controller.payload.NewTaskRequest;
import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.controller.payload.TaskUpdateRequest;
import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import com.thiscompany.ttrack.enums.converter.EnumMapper;
import com.thiscompany.ttrack.exceptions.TaskNotFoundException;
import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.repository.TaskRepository;
import com.thiscompany.ttrack.service.task.TaskService;
import com.thiscompany.ttrack.service.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepo;

    @Transactional
    @Override
    public TaskResponse createTask(NewTaskRequest request) {
        Task task = taskMapper.requestToEntity(request);
        setStatusAndState(task, request.status());
        setTaskPriority(task, request.priority());
        taskRepo.save(task);
        log.info("Obj created with id {}", task.getId());
        return taskMapper.entityToResponse(task);
    }

    @Override
    public TaskResponse getTask(Long id) {
        Task task = taskRepo.findTaskById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));
        return taskMapper.entityToResponse(task);
    }

    @Transactional
    @Override
    public TaskResponse updateTask(Long id, TaskUpdateRequest updateRequest) {
        Task taskToUpdate = taskRepo.findTaskById(id)
                .orElseThrow(()->new TaskNotFoundException(id));
        taskMapper.patchEntity(updateRequest, taskToUpdate);
        taskRepo.save(taskToUpdate);
        log.info("Obj with id {} updated, [payload: {}]", id, taskToUpdate.toString());
        return taskMapper.entityToResponse(taskToUpdate);
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        if(taskRepo.existsById(id)) {
            taskRepo.deleteById(id);
            log.info("Obj with id {} removed", id);
        }
        else throw new TaskNotFoundException(id);
    }

    private void setStatusAndState(Task task, String status) {
        if (status == null) {
            task.setStatus(TaskStatus.IN_PROGRESS);
            task.setState(TaskState.ACTIVE);
        }
        else if (status.equalsIgnoreCase("draft")) {
            task.setStatus(TaskStatus.DRAFT);
            task.setState(TaskState.INITIAL);
        }
    }

    private void setTaskPriority(Task task, String priority) {
        if (priority == null) {
            task.setPriority(Priority.NORMAL);
            return;
        }
        else task.setPriority(EnumMapper.mapToPriority(priority));
    }
}
