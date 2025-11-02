package com.thiscompany.ttrack.utils.constant;

import com.thiscompany.ttrack.model.Permission;
import com.thiscompany.ttrack.repository.PermissionRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PermissionConstantInitializer {
	
	private static Permission DEFAULT_PERMISSION;
	private static final PermissionConstantInitializer INSTANCE = new PermissionConstantInitializer();
	
	private PermissionConstantInitializer() {}
	
	@EventListener(classes = ApplicationReadyEvent.class)
	public void initializeSettingUp(ApplicationReadyEvent event) {
		short id = 1;
		PermissionConstantInitializer.DEFAULT_PERMISSION =
			event.getApplicationContext().getBean(PermissionRepository.class).findPermissionById(id);
	}
	
	public static Permission getDefaultPermission() {
		return DEFAULT_PERMISSION;
	}
}
