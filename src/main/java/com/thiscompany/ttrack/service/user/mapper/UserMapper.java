package com.thiscompany.ttrack.service.user.mapper;

import com.thiscompany.ttrack.controller.user.dto.UserCreationRequest;
import com.thiscompany.ttrack.controller.user.dto.UserResponse;
import com.thiscompany.ttrack.controller.user.dto.UserUpdateBody;
import com.thiscompany.ttrack.enums.SystemAuthority;
import com.thiscompany.ttrack.model.Permission;
import com.thiscompany.ttrack.model.User;
import com.thiscompany.ttrack.model.UserPermission;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mappings({
            @Mapping(target= "permissions", source = "permissions", qualifiedByName = "mapFromPermissionsToStringSet"),
    } )
    UserResponse entityToResponse(User user);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(request.password()))")
    User createRequestToEntity(UserCreationRequest request, @Context PasswordEncoder passwordEncoder);

    @Mapping(target = "permissions", ignore = true)
    void patchEntity(UserUpdateBody request, @MappingTarget User user);

    @Named("mapFromPermissionsToStringSet")
    default Set<String> mapUserPermissionSetToStringSet(Set<UserPermission> authorities) {
        return authorities.stream()
                .map(userPermission -> userPermission.getPermission().getName())
                .map(SystemAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    @Named("mapListStringToUserPermission")
    default Set<UserPermission> mapStringSetToUserPermissionSet(Set<String> authorities) {
        return authorities.stream()
                .map(roleName -> {
                    Permission permission = new Permission();
                    permission.setName(SystemAuthority.valueOf(roleName));
                    UserPermission userPermission = new UserPermission();
                    userPermission.setPermission(permission);
                    return userPermission;
                })
                .collect(Collectors.toSet());
    }
}
