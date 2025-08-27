package com.thiscompany.ttrack.service.task.impl;

import com.thiscompany.ttrack.controller.task.dto.NewTaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskResponse;
import com.thiscompany.ttrack.controller.task.dto.TaskUpdateRequest;
import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import com.thiscompany.ttrack.enums.mapper.MapperUtils;
import com.thiscompany.ttrack.exceptions.illegal.TaskIllegalStateException;
import com.thiscompany.ttrack.exceptions.not_found.TaskNotFoundException;
import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.repository.TaskRepository;
import com.thiscompany.ttrack.repository.specification.CustomSpecifications;
import com.thiscompany.ttrack.service.task.TaskService;
import com.thiscompany.ttrack.service.task.mapper.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

   private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

   private final TaskRepository taskRepo;
   private final TaskMapper taskMapper;


   @Transactional
   @Override
   public TaskResponse createTask(NewTaskRequest request, User user) {
      Task task = taskMapper.requestToEntity(request);
      task.setStatus(TaskStatus.ONGOING)
          .setState(TaskState.ACTIVE)
          .setPriority(applyPriority(request.priority()))
          .setOwner(user);
      taskRepo.save(task);
      log.info("Task created with id {}", task.getId());
      return taskMapper.entityToResponse(task);
   }

   @Transactional
   @Override
   public TaskResponse createDraftTask(NewTaskRequest request, User user) {
      Task task = taskMapper.requestToEntity(request);
      task.setStatus(TaskStatus.DRAFT)
          .setState(TaskState.INITIAL)
          .setPriority(applyPriority(request.priority()))
          .setOwner(user);
      taskRepo.save(task);
      log.info("Task created with id {} ", task.getId());
      return taskMapper.entityToResponse(task);
   }

   @Override
   public TaskResponse findTaskById(String id, String requestUser) {
      Task task = findById(id, requestUser);
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
      Specification<Task> spec = CustomSpecifications.filterByUserAndParam(
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
      if(taskToUpdate.getIsCompleted() == true) {
          throw new TaskIllegalStateException("task.error.upon.update");
      }
      taskMapper.patchEntity(updateRequest, taskToUpdate);
      taskRepo.save(taskToUpdate);
      log.debug("Task updated with id {}, [payload: {}]", id, taskToUpdate);
      return taskMapper.entityToResponse(taskToUpdate);
   }

   @Transactional
   @Override
   public void deleteTask(String id, String requestUser) {
      Specification<Task> spec = CustomSpecifications.filterByUserAndParam(
              requestUser,
              root -> root.get("id"),
              id
      );
      if(taskRepo.exists(spec)) {
         taskRepo.deleteTaskById(id);
         log.debug("Obj removed with taskId {}", id);
      }
      else throw new TaskNotFoundException(id);
   }

    @Override
    public Page<TaskResponse> findTasksByParams(
        String requestUser, String title, String details, String status,
        String state, String priority, boolean completed, Pageable pageable
    ) {
       Specification<Task> spec = CustomSpecifications
                .filterByParam(requestUser, title, details, status, state, priority, completed);
       return taskRepo.findAll(spec, pageable)
                      .map(taskMapper::entityToResponse);
    }

    private Priority applyPriority(String priority) {
      if (priority == null) {
         return Priority.NORMAL;
      }
      else return MapperUtils.mapToPriority(priority);
   }

   private Task findById(String id, String requestUser) {
      Specification<Task> spec = CustomSpecifications.filterByUserAndParam(
              requestUser,
              root -> root.get("id"),
              id
      );
      return taskRepo.findOne(spec)
              .orElseThrow(()->new TaskNotFoundException(id));
   }

   private List<Task> findAllTasks(String requestUser) {
      return taskRepo.findAll(CustomSpecifications.filterByAuthUser(requestUser));
   }
}
