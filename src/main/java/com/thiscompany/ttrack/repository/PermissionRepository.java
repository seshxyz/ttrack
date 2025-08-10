package com.thiscompany.ttrack.repository;

import com.thiscompany.ttrack.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository extends JpaRepository<Permission, Short>, JpaSpecificationExecutor<Permission> {

    Permission findPermissionById(Short id);

}
