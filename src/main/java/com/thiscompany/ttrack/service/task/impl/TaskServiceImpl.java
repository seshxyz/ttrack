package com.thiscompany.ttrack.service.task.impl;

import com.thiscompany.ttrack.controller.task.dto.NewTaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskResponse;
import com.thiscompany.ttrack.controller.task.dto.TaskUpdateRequest;
import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import com.thiscompany.ttrack.enums.mapper.MapperUtils;
import com.thiscompany.ttrack.exceptions.TaskNotFoundException;
import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.repository.TaskRepository;
import com.thiscompany.ttrack.repository.specification.CustomSpecification;
import com.thiscompany.ttrack.service.task.TaskService;
import com.thiscompany.ttrack.service.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LogManager.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepo;
    private final TaskMapper taskMapper;


    @Transactional
    @Override
    public TaskResponse createTask(NewTaskRequest request) {
        Task task = taskMapper.requestToEntity(request);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        applyStatusAndStateOnCreation(task, request.status());
        setTaskPriority(task, request.priority());
        task.setOwner(user);
        taskRepo.save(task);
        log.info("task created with id {}", task.getId());
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
        log.info("Task with id {} created", task.getId());
        return taskMapper.entityToResponse(task);
    }

    @Override
    public TaskResponse findTaskById(TaskRequest request, String requestUser) {
        Task task = findById(request.id(), requestUser);
        return taskMapper.entityToResponse(task);
    }

    @Override
    public List<TaskResponse> findAllUserTasks(String requestUser) {
        return findAllTasks(requestUser)
                .stream()
                .map(taskMapper::entityToResponse)
                .toList();
    }


    @Override
    public List<TaskResponse> findAllUserActiveTasks(String requestUser){
        Specification<Task> spec = CustomSpecification.filterByUserAndParam(
                requestUser,
                root -> root.get("state"),
                TaskState.ACTIVE
        );
        return taskRepo.findAll(spec).stream()
                .map(taskMapper::entityToResponse)
                .toList();
    }

    @Transactional
    @Override
    public TaskResponse updateTask(String id, TaskUpdateRequest updateRequest, String requestUser) {
        Task taskToUpdate = findById(id, requestUser);
        taskMapper.patchEntity(updateRequest, taskToUpdate);
        taskRepo.save(taskToUpdate);
        log.info("Task with id {} updated, [payload: {}]", id, taskToUpdate);
        return taskMapper.entityToResponse(taskToUpdate);
    }

    @Transactional
    @Override
    public void deleteTask(String id, String requestUser) {
        Specification<Task> spec = CustomSpecification.filterByUserAndParam(
                requestUser,
                root -> root.get("id"),
                id
        );
        if(taskRepo.exists(spec)) {
            taskRepo.deleteTaskById(id);
            log.info("Obj with userId {} removed", id);
        }
        else throw new TaskNotFoundException(id);
    }

    private void applyStatusAndStateOnCreation(Task task, String status) {
        if (status.equals("draft")) {
            task.setStatus(TaskStatus.DRAFT);
            task.setState(TaskState.INITIAL);
        } else {
            task.setStatus(TaskStatus.ONGOING);
            task.setState(TaskState.ACTIVE);
        }
    }

    private void setTaskPriority(Task task, String priority) {
        if (priority == null) {
            task.setPriority(Priority.NORMAL);
        }
        else task.setPriority(MapperUtils.mapToPriority(priority));
    }

    private Task findById(String id, String requestUser) {
        Specification<Task> spec = CustomSpecification.filterByUserAndParam(
                requestUser,
                root -> root.get("id"),
                id
        );
        return taskRepo.findOne(spec)
                .orElseThrow(()->new TaskNotFoundException(id));
    }

    private List<Task> findAllTasks(String requestUser) {
        return taskRepo.findAll(CustomSpecification.filterByAuthUser(requestUser));
    }
}
