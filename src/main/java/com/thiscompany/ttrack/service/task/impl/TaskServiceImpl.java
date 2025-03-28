package com.thiscompany.ttrack.service.task.impl;

import com.thiscompany.ttrack.controller.payload.NewTaskRequest;
import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.controller.payload.TaskUpdateRequest;
import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import com.thiscompany.ttrack.enums.mapper.EnumMapper;
import com.thiscompany.ttrack.exceptions.TaskNotFoundException;
import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.repository.TaskRepository;
import com.thiscompany.ttrack.service.task.AbstractTaskFinder;
import com.thiscompany.ttrack.service.task.TaskService;
import com.thiscompany.ttrack.service.task.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class TaskServiceImpl extends AbstractTaskFinder implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepo;

    public TaskServiceImpl(TaskRepository taskRepo, TaskMapper taskMapper) {
        super(taskRepo);
        this.taskMapper = taskMapper;
        this.taskRepo = taskRepo;
    }

    @Transactional
    @Override
    public TaskResponse createTask(NewTaskRequest request) {
        Task task = taskMapper.requestToEntity(request);
        flexibleSetStatusAndState(task, request.status());
        setTaskPriority(task, request.priority());
        taskRepo.save(task);
        log.info("Obj created with id {}", task.getId());
        return taskMapper.entityToResponse(task);
    }

    @Transactional
    @Override
    public TaskResponse createDraftTask(NewTaskRequest request) {
        Task task = taskMapper.requestToEntity(request);
        task.setStatus(TaskStatus.DRAFT);
        task.setState(TaskState.INITIAL);
        setTaskPriority(task, request.priority());
        taskRepo.save(task);
        log.info("Obj created with id {}", task.getId());
        return taskMapper.entityToResponse(task);
    }

    @Override
    public TaskResponse getTask(Long id) {
        Task task = findTaskById(id);
        return taskMapper.entityToResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTask() {
        return findAllTasks()
                .stream()
                .map(taskMapper::entityToResponse)
                .toList();
    }

    @Transactional
    @Override
    public TaskResponse updateTask(Long id, TaskUpdateRequest updateRequest) {
        Task taskToUpdate = findTaskById(id);
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

    private void flexibleSetStatusAndState(Task task, String status) {
        if (status == null || status.equalsIgnoreCase("ongoing")) {
            task.setStatus(TaskStatus.ONGOING);
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
