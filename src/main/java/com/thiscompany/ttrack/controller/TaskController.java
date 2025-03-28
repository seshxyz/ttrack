package com.thiscompany.ttrack.controller;

import com.thiscompany.ttrack.controller.payload.NewTaskRequest;
import com.thiscompany.ttrack.controller.payload.TaskResponse;
import com.thiscompany.ttrack.controller.payload.TaskUpdateRequest;
import com.thiscompany.ttrack.service.task.TaskProcessing;
import com.thiscompany.ttrack.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskProcessing taskProcessing;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findTaskById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(taskService.getTask(id), HttpStatus.FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> findAllTasks() {
        return new ResponseEntity<>(
                taskService.getAllTask(),
                HttpStatus.FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody NewTaskRequest request) {
        return new ResponseEntity<>(
                taskService.createTask(request),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/draft")
    public ResponseEntity<TaskResponse> createDraftTask(
            @RequestParam("draft") boolean draft,
            @Valid @RequestBody NewTaskRequest request
    ) {
        return new ResponseEntity<>(
                taskService.createDraftTask(request),
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
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/promote")
    public ResponseEntity<?> promoteTask(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                taskProcessing.promoteTask(id),
                HttpStatus.OK
        );
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelTask(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                taskProcessing.cancelTask(id),
                HttpStatus.OK
        );
    }

}
