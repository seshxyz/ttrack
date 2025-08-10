package com.thiscompany.ttrack.controller.task;

import com.thiscompany.ttrack.controller.task.dto.NewTaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskRequest;
import com.thiscompany.ttrack.controller.task.dto.TaskResponse;
import com.thiscompany.ttrack.controller.task.dto.TaskUpdateRequest;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.service.task.TaskProcessing;
import com.thiscompany.ttrack.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Task API", description = "Endpoints for task management")
public class TaskController {

    private final TaskService taskService;
    private final TaskProcessing taskProcessing;

    @Operation(summary = "Find task by id", description = "Method to find a task by id")
    @GetMapping("/")
    public ResponseEntity<TaskResponse> findTaskById(@NotEmpty @RequestBody TaskRequest request,
                                                     @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(
                taskService.findTaskById(request, user.getUsername()),
                HttpStatus.FOUND
        );
    }

    @Operation(summary = "Find all current user's tasks", description = "Method to find all current user's tasks")
    @GetMapping("/all")
    public ResponseEntity<List<TaskResponse>> findAllTasks(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(
                taskService.findAllUserTasks(user.getUsername()),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Find all current user's tasks", description = "Method to find all current user's tasks")
    @GetMapping("/active")
    public ResponseEntity<List<TaskResponse>> findAllActiveTasks(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(
                taskService.findAllUserActiveTasks(user.getUsername()),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Create new task", description = "Method to create a new task")
    @PostMapping("/")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody NewTaskRequest request) {
        return new ResponseEntity<>(
                taskService.createTask(request),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Create new draft task", description = "Method to create a new draft task")
    @PostMapping("/draft")
    public ResponseEntity<TaskResponse> createDraftTask(
            @Valid @RequestBody NewTaskRequest request
    ) {
        return new ResponseEntity<>(
                taskService.createDraftTask(request),
                HttpStatus.CREATED
        );
    }

    @Operation(summary = "Update task", description = "Method to update a task")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable("id") String id,
            @Valid @RequestBody TaskUpdateRequest request,
            @AuthenticationPrincipal Authentication requestUser
    ) {
        return new ResponseEntity<>(
                taskService.updateTask(id, request, requestUser.getName()),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Delete task", description = "Method to delete a task")
    @DeleteMapping(value = "/")
    public ResponseEntity<?> deleteTask(
            @RequestBody String id,
            @AuthenticationPrincipal User user) {
        taskService.deleteTask(id, user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Promote task", description = "Method to promote a task further")
    @PostMapping("/promote")
    public ResponseEntity<?> promoteTask(
            @NotEmpty @RequestBody String id, @AuthenticationPrincipal User user
    ) {
        return new ResponseEntity<>(
                taskProcessing.promoteTask(id, user.getUsername()),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Cancel task", description = "Method to cancel a task from running")
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelTask(
            @NotEmpty @RequestBody String id, @AuthenticationPrincipal User user
    ) {
        return new ResponseEntity<>(
                taskProcessing.cancelTask(id, user.getUsername()),
                HttpStatus.OK
        );
    }

}
