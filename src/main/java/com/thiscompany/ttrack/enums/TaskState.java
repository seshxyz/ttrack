package com.thiscompany.ttrack.enums;

public enum TaskState implements GenericEnumFunction<TaskState> {

    INITIAL, ACTIVE, EXPIRED, INACTIVE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
