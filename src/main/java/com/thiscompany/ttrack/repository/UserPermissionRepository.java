package com.thiscompany.ttrack.repository;

import com.thiscompany.ttrack.model.UserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Short> {

    @Override
    UserPermission getReferenceById(Short id);

}
