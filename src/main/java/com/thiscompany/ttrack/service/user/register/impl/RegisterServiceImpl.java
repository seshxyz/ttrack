package com.thiscompany.ttrack.service.user.register.impl;

import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.exceptions.exist.UserAlreadyExistException;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.model.UserPermission;
import com.thiscompany.ttrack.repository.UserRepository;
import com.thiscompany.ttrack.service.user.mapper.UserMapper;
import com.thiscompany.ttrack.service.user.register.RegisterService;
import com.thiscompany.ttrack.utils.constant.PermissionConstantInitializer;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
	
	private final UserRepository userRepo;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void register(UserCreationRequest request) {
		User newUser = userMapper.createRequestToEntity(request, passwordEncoder);
		UserPermission userPermission = new UserPermission();
		userPermission.setUser_name(newUser)
					  .setPermission(PermissionConstantInitializer.getDefaultPermission());
		newUser.setPermissions(Set.of(userPermission));
		try {
			userRepo.save(newUser);
		} catch (DataIntegrityViolationException rootException) {
			if(rootException.getCause() instanceof ConstraintViolationException databaseException) {
				if("users_username_key".equals(databaseException.getConstraintName())) {
					throw new UserAlreadyExistException("user.already_exist",new Object[]{request.username()});
				}
			}
		}
	}
	
}
