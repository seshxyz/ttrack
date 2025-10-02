package com.thiscompany.ttrack.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum SystemAuthority implements GenericEnumFunction<SystemAuthority>, GrantedAuthority {
	
	ROLE_BASE_USER, ROLE_ADMIN;
	
	@Override
	public String toString() {
		return name().toUpperCase();
	}
	
	@JsonValue
	@Override
	public String getAuthority() {
		return this.toString();
	}
}
