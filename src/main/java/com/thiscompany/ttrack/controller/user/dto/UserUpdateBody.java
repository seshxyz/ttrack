package com.thiscompany.ttrack.controller.user.dto;

import java.util.Set;

public record UserUpdateBody(
	
	String password,
	Set<String> permissions,
	boolean isActive

) {

}
