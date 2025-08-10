package com.thiscompany.ttrack.service.user.impl;

import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.controller.user.dto.UserResponse;
import com.thiscompany.ttrack.controller.user.dto.UserUpdateRequest;
import com.thiscompany.ttrack.exceptions.UserNotFoundException;
import com.thiscompany.ttrack.model.Permission;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.model.UserPermission;
import com.thiscompany.ttrack.repository.PermissionRepository;
import com.thiscompany.ttrack.repository.UserRepository;
import com.thiscompany.ttrack.service.const_utils.Constants;
import com.thiscompany.ttrack.service.user.UserService;
import com.thiscompany.ttrack.service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PermissionRepository permissionRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    @PreAuthorize("{hasRole('ADMIN')}")
    @Override
    public UserResponse createUser(UserCreationRequest request) {
        User newUser = userMapper.createRequestToEntity(request, passwordEncoder);
        UserPermission userPermission = new UserPermission();
        Permission permission = permissionRepo.findPermissionById(Constants.DEFAULT_PERMISSION_ID);
        userPermission.setUser_name(newUser);
        userPermission.setPermission(permission);
        newUser.setPermissions(Set.of(userPermission));
        userRepo.save(newUser);
        return userMapper.entityToResponse(newUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse findByName(String username) {
        return findUserByName(username);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse findById(String id) {
        return userRepo.findById(id)
                .map(userMapper::entityToResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserResponse updateUser(UserUpdateRequest request) {
        User updatedUser = findUserById(request.id())
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
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteUser(String id) {
        findUserById(id).ifPresentOrElse(
                userRepo::delete,
                ()->{throw new UserNotFoundException(id);}
        );
    }

    private UserResponse findUserByName(String username) {
        return userRepo.findUserByUsername(username)
                .map(userMapper::entityToResponse)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Optional<User> findUserById(String id) {
        return userRepo.findUserById(id);
    }

}
