package com.thiscompany.ttrack.service.user;

import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.controller.user.dto.UserResponse;
import com.thiscompany.ttrack.controller.user.dto.UserUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    UserResponse createUser(UserCreationRequest request);

    UserResponse findByName(String username);

    UserResponse findById(String id);

    UserResponse updateUser(UserUpdateRequest request);

    void deleteUser(String username);

}
