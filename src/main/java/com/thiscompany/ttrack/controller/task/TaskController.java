package com.thiscompany.ttrack.controller.task;

import com.thiscompany.ttrack.controller.task.dto.NewTaskRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Task API", description = "Endpoints for task management")
public class TaskController {
	
	private final TaskService taskService;
	private final TaskProcessing taskProcessing;
	
	@Operation(summary = "Find task by id", description = "Method to find a task by id")
	@GetMapping("/{id}")
	public ResponseEntity<TaskResponse> findTaskById(
		@NotEmpty(message = "field.empty_id") @PathVariable("id") String id,
		Principal requestUser
	) {
		return ResponseEntity.ok(taskService.findTaskById(id, requestUser.getName()));
	}
	
	@Operation(summary = "Find all current user's tasks", description = "Method to find all current user's tasks")
	@GetMapping("/all")
	public ResponseEntity<List<TaskResponse>> findAllTasks(Principal requestUser) {
		return ResponseEntity.ok(taskService.findAllUserTasks(requestUser.getName()));
	}
	
	@Operation(summary = "Find all current user's active tasks", description = "Method to find all current user's tasks")
	@GetMapping("/active")
	public ResponseEntity<List<TaskResponse>> findAllActiveTasks(Principal requestUser) {
		return ResponseEntity.ok(taskService.findAllUserActiveTasks(requestUser.getName()));
	}
	
	@Operation(summary = "Create new task", description = "Method to create a new task")
	@PostMapping("/")
	public ResponseEntity<TaskResponse> createTask(
		@Valid @RequestBody NewTaskRequest request, @AuthenticationPrincipal User user) {
		return new ResponseEntity<>(
			taskService.createTask(request, user),
			HttpStatus.CREATED
		);
	}
	
	@Operation(summary = "Create new draft task", description = "Method to create a new draft task")
	@PostMapping("/draft")
	public ResponseEntity<TaskResponse> createDraftTask(
		@Valid @RequestBody NewTaskRequest request, @AuthenticationPrincipal User user
	) {
		return new ResponseEntity<>(
			taskService.createDraftTask(request, user),
			HttpStatus.CREATED
		);
	}
	
	@Operation(summary = "Update task", description = "Method to update a task")
	@PatchMapping(value = "/{id}/update")
	public ResponseEntity<TaskResponse> updateTask(
		@NotEmpty(message = "field.empty_id") @PathVariable("id") String id,
		@Valid @RequestBody TaskUpdateRequest request,
		Principal requestUser
	) {
		return ResponseEntity.ok(taskService.updateTask(id, request, requestUser.getName()));
	}
	
	@Operation(summary = "Delete task", description = "Method to delete a task")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteTask(
		@NotEmpty(message = "field.empty_id") @PathVariable("id") String id,
		Principal requestUser
	) {
		taskService.deleteTask(id, requestUser.getName());
		return ResponseEntity.noContent()
							 .build();
	}
	
	@Operation(summary = "Promote task", description = "Method to promote a task further")
	@PostMapping("/{id}/promote")
	public ResponseEntity<?> promoteTask(
		@NotEmpty(message = "field.empty_id") @PathVariable("id") String id,
		Principal requestUser
	) {
		return ResponseEntity.ok(taskProcessing.promoteTask(id, requestUser.getName()));
	}
	
	@Operation(summary = "Cancel task", description = "Method to cancel a task from running")
	@PostMapping("/{id}/cancel")
	public ResponseEntity<?> cancelTask(
		@NotEmpty(message = "field.empty_id") @PathVariable("id") String id,
		Principal requestUser
	) {
		return ResponseEntity.ok(taskProcessing.cancelTask(id, requestUser.getName()));
	}
	
	@Operation(summary = "Find task(s) with parameters", description = "Method to find task(s) with parameters")
	@GetMapping("/find_by")
	public ResponseEntity<Page<TaskResponse>> findTasksWithParams(
		Principal requestUser,
		@RequestParam(value = "title", required = false) String title,
		@RequestParam(value = "details", required = false) String details,
		@RequestParam(value = "status", required = false) String status,
		@RequestParam(value = "state", required = false) String state,
		@RequestParam(value = "priority", required = false) String priority,
		@RequestParam(value = "isCompleted", required = false) boolean isCompleted,
		Pageable pageable
	) {
		return ResponseEntity
				   .ok(taskService.findTasksByParams(
					   requestUser.getName(), title, details, status, state, priority, isCompleted, pageable)
				   );
	}
	
}
