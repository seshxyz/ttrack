package com.thiscompany.ttrack.service.task.impl;

import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.repository.TaskRepository;
import com.thiscompany.ttrack.service.task.AbstractTaskFinder;
import com.thiscompany.ttrack.service.task.TaskProcessing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class TaskProcessingImpl extends AbstractTaskFinder implements TaskProcessing {

    private final TaskRepository taskRepo;

    public TaskProcessingImpl(TaskRepository taskRepo) {
        super(taskRepo);
        this.taskRepo = taskRepo;
    }

    @Transactional
    @Override
    public ResponseEntity<?> promoteTask(Long id) {
        Task task = findTaskById(id);
        final int statusIndex = task.getStatus().ordinal();
        boolean statusChanged = false;
        if(task.getStatus().equals(TaskStatus.COMPLETED) || task.getStatus().equals(TaskStatus.CANCELED)) {
            return response(id, task.getStatus(), statusChanged);
        }
        else {
            List<TaskStatus> statusList = Arrays.asList(Arrays.copyOfRange(
                    TaskStatus.values(),
                    0,
                    TaskStatus.values().length
            ));
            task.setStatus(statusList.get(statusIndex + 1));
            statusChanged = true;
            task.setCompleted(completeIfFinal(task.getStatus()));
            task.setState(setInactiveIfFinal(task.getState(), task.getCompleted()));
            taskRepo.saveAndFlush(task);
            return response(id, task.getStatus(), statusChanged);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> cancelTask(Long id) {
        Task task = findTaskById(id);
        boolean statusChanged = false;
        if(!task.getStatus().equals(TaskStatus.CANCELED)){
            task.setStatus(TaskStatus.CANCELED);
            statusChanged = true;
            task.setState(TaskState.INACTIVE);
            return response(id, task.getStatus(), statusChanged);
        }
        return response(id, task.getStatus(), statusChanged);
    }

    private boolean completeIfFinal(TaskStatus status) {
        return status.equals(TaskStatus.COMPLETED);
    }

    private TaskState setInactiveIfFinal(TaskState state, boolean completed){
        if(completed) return TaskState.INACTIVE;
        else return state;
    }

    private ResponseEntity<?> response(Long id, TaskStatus status, boolean statusChanged) {
        if((status.equals(TaskStatus.COMPLETED)
                || status.equals(TaskStatus.CANCELED)) && !statusChanged) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Task already completed or canceled");
        }
        if(statusChanged) {
            if(status.equals(TaskStatus.COMPLETED)) {
                return ResponseEntity.ok("Task has been completed");
            } else if(status.equals(TaskStatus.CANCELED)) {
                return ResponseEntity.ok("Task has been canceled");
            }
            else return ResponseEntity.ok("Task promoted further. Current status: " + status);
        }
        log.error(
                "Problem while building response. Payload: task_id: {}, status: {}, statusChanged: {}",
                id, status, statusChanged
        );
        return ResponseEntity.badRequest().build();
    }


}
