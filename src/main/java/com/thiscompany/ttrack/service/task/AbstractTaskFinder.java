package com.thiscompany.ttrack.service.task;

import com.thiscompany.ttrack.exceptions.TaskNotFoundException;
import com.thiscompany.ttrack.model.Task;
import com.thiscompany.ttrack.repository.TaskRepository;

import java.util.List;

public abstract class AbstractTaskFinder {

    protected final TaskRepository taskRepo;

    protected AbstractTaskFinder(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    protected Task findTaskById(Long id) {
       return taskRepo.findTaskById(id)
               .orElseThrow(()-> new TaskNotFoundException(id));
    }

    protected List<Task> findAllTasks() {
        return taskRepo.findAll();
    }

}
