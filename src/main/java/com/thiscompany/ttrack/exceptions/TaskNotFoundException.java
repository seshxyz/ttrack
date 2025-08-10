package com.thiscompany.ttrack.exceptions;

import com.thiscompany.ttrack.exceptions.base.NotFoundException;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(String id) {
        super("task.not.found", new Object[]{id});
    }

}
