package com.thiscompany.ttrack.exceptions.not_found;

import com.thiscompany.ttrack.exceptions.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
	
	public UserNotFoundException(String nameOrId) {
		super("user.not_found", new Object[]{nameOrId});
	}
	
}
