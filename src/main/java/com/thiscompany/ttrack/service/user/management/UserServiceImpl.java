package com.thiscompany.ttrack.service.user.management;

import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.controller.user.dto.UserResponse;
import com.thiscompany.ttrack.controller.user.dto.UserUpdateRequest;
import com.thiscompany.ttrack.exceptions.not_found.UserNotFoundException;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.model.UserPermission;
import com.thiscompany.ttrack.repository.UserRepository;
import com.thiscompany.ttrack.service.user.UserService;
import com.thiscompany.ttrack.service.user.mapper.UserMapper;
import com.thiscompany.ttrack.utils.constant.PermissionConstantInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;
	
	@Transactional
	@Override
	public UserResponse createUser(UserCreationRequest request) {
		User newUser = userMapper.createRequestToEntity(request, passwordEncoder);
		UserPermission userPermission =
			new UserPermission().setUser_name(newUser)
								.setPermission(PermissionConstantInitializer.getDefaultPermission());
		newUser.setPermissions(Set.of(userPermission));
		userRepo.save(newUser);
		return userMapper.entityToResponse(newUser);
	}
	
	@Override
	public UserResponse findByName(String username) {
		return userRepo.findUserByUsername(username)
					   .map(userMapper::entityToResponse)
					   .orElseThrow(() -> new UserNotFoundException(username));
	}
	
	@Override
	public UserResponse findById(String id) {
		return userRepo.findUserById(id)
					   .map(userMapper::entityToResponse)
					   .orElseThrow(() -> new UserNotFoundException(id));
	}
	
	@Transactional
	@Override
	public UserResponse updateUser(UserUpdateRequest request) {
		User updatedUser = userRepo.findUserById(request.id())
							   .orElseThrow(() -> new UserNotFoundException(request.id()));
		Set<UserPermission> permissions = updatedUser.getPermissions();
		userMapper.patchEntity(request.data(), updatedUser);
		Set<UserPermission> newPermissions =
			userMapper.mapStringSetToUserPermissionSet(request.data().permissions());
		permissions.addAll(newPermissions);
		updatedUser.setPermissions(permissions);
		userRepo.save(updatedUser);
		return userMapper.entityToResponse(updatedUser);
	}
	
	@Transactional
	@Override
	public void deleteUser(String id) {
		userRepo.findUserById(id).ifPresentOrElse(
			userRepo::delete,
			() -> {throw new UserNotFoundException(id);}
		);
	}
}
