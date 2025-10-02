package com.thiscompany.ttrack.utils.constant;

import com.thiscompany.ttrack.model.Permission;
import com.thiscompany.ttrack.repository.PermissionRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PermissionConstant {
	
	private static Permission DEFAULT_PERMISSION_ID;
	
	@EventListener(classes = ApplicationReadyEvent.class)
	public void initializeSettingUp(ApplicationReadyEvent event) {
		short id = 1;
		PermissionConstant.DEFAULT_PERMISSION_ID =
			event.getApplicationContext().getBean(PermissionRepository.class).findPermissionById(id);
	}
	
	public static Permission getDefaultPermission() {
		return DEFAULT_PERMISSION_ID;
	}
	
}
