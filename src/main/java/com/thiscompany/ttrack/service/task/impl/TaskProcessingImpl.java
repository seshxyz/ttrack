package com.thiscompany.ttrack.service.task.impl;

import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import com.thiscompany.ttrack.exceptions.TaskNotFoundException;
import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.repository.TaskRepository;
import com.thiscompany.ttrack.repository.specification.CustomSpecification;
import com.thiscompany.ttrack.service.task.TaskProcessing;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.thiscompany.ttrack.enums.TaskStatus.COMPLETED;

@Service
@RequiredArgsConstructor
public class TaskProcessingImpl implements TaskProcessing {

    private final Logger logger = LoggerFactory.getLogger(TaskProcessingImpl.class);

    private final TaskRepository taskRepo;

    @Transactional
    @Override
    public ResponseEntity<?> promoteTask(String id, String requestUser) {
        Task task = findById(id, requestUser);
        TaskStatus currentStatus = task.getStatus();
        boolean isStatusChanged = false;
        if(isCompletedOrCanceled(currentStatus)) {
            return response(id, currentStatus, isStatusChanged);
        }
        else {
            task.setStatus(currentStatus.getNextFrom(currentStatus));
            isStatusChanged = true;
            task.setCompleted(completeIfFinal(task.getStatus()));
            task.setState(setInactiveIfFinal(task.getState(), task.isCompleted()));
            taskRepo.saveAndFlush(task);
            return response(id, task.getStatus(), isStatusChanged);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> cancelTask(String id, String requestUser) {
        Task task = findById(id, requestUser);
        boolean isStatusChanged = false;
        if(!task.getStatus().equals(TaskStatus.CANCELED)){
            task.setStatus(TaskStatus.CANCELED);
            isStatusChanged = true;
            task.setState(TaskState.INACTIVE);
            return response(id, task.getStatus(), isStatusChanged);
        }
        return response(id, task.getStatus(), isStatusChanged);
    }

    private boolean completeIfFinal(TaskStatus status) {
        return status.equals(COMPLETED);
    }

    private boolean isCompletedOrCanceled(TaskStatus status) {
        return status.equals(COMPLETED) || status.equals(TaskStatus.CANCELED);
    }

    private TaskState setInactiveIfFinal(TaskState state, boolean isCompleted){
        if(isCompleted) return TaskState.INACTIVE;
        else return state;
    }

    private ResponseEntity<?> response(String id, TaskStatus status, boolean isStatusChanged) {
        if(isCompletedOrCanceled(status) && !isStatusChanged) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Task already completed or canceled");
        }
        else if(isStatusChanged) {
           return switch (status) {
                case COMPLETED -> ResponseEntity.ok("Task has been completed");
                case CANCELED -> ResponseEntity.ok("Task has been canceled");
                default -> ResponseEntity.ok("Task promoted further. Current status: " + status);
            };
        }
        logger.error(
                "Problem while building response. Payload: task_id: {}, status: {}, isStatusChanged: {}",
                id, status, isStatusChanged
        );
        return ResponseEntity.badRequest().build();
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

}
