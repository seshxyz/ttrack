package com.thiscompany.ttrack.controller;

import com.thiscompany.ttrack.controller.payload.NewTaskRequest;
import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.controller.payload.TaskUpdateRequest;
import com.thiscompany.ttrack.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findTaskById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(taskService.getTask(id), HttpStatus.FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody NewTaskRequest request) {
        return new ResponseEntity<>(
                taskService.createTask(request),
                HttpStatus.CREATED
        );
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable("id") Long id, @Valid @RequestBody TaskUpdateRequest request
    ) {
        return new ResponseEntity<>(
                taskService.updateTask(id, request),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
