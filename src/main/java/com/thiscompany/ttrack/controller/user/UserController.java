package com.thiscompany.ttrack.controller.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.controller.user.dto.UserResponse;
import com.thiscompany.ttrack.controller.user.dto.UserUpdateRequest;
import com.thiscompany.ttrack.model.view.DtosViews;
import com.thiscompany.ttrack.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Endpoints for user management")
public class UserController {
	
	private final UserService userService;
	
	@Operation(summary = "Find user by username", description = "Method to find a user by username")
	@GetMapping("/{username}")
	public ResponseEntity<UserResponse> findUserByUsername(@NotEmpty @PathVariable("username") String username) {
		return ResponseEntity.ok(userService.findByName(username));
	}
	
	@Operation(summary = "Find user by id", description = "Method to find a user by username")
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> findUserById(@NotEmpty @PathVariable("id") String id) {
		return ResponseEntity.ok(userService.findById(id));
	}
	
	
	@Operation(summary = "Create new user", description = "Method to create a new user")
	@PostMapping("/")
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreationRequest request) {
		return ResponseEntity.ok(userService.createUser(request));
	}
	
	@Operation(summary = "Update user", description = "Method to update a user")
	@PostMapping("/update")
	public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateRequest request) {
		return ResponseEntity.ok(userService.updateUser(request));
	}
	
	@Operation(summary = "Delete user", description = "Method to delete a user")
	@DeleteMapping("/")
	public ResponseEntity<?> deleteUser(@RequestParam("username") String username) {
		userService.deleteUser(username);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "Find current authorized user", description = "Method to find the current user")
	@GetMapping("/me")
	@JsonView(DtosViews.UserBaseView.class)
	public ResponseEntity<UserResponse> findCurrentUser(Principal requestUser) {
		return ResponseEntity.ok(userService.findByName(requestUser.getName()));
	}
	
}
