package com.thiscompany.ttrack.exceptions;

import com.thiscompany.ttrack.exceptions.base.NotFoundException;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException(Long id) {
        super("task.not.found", new Object[]{id});
    }

}
