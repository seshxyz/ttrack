package com.thiscompany.ttrack.enums;

public enum TaskStatus implements GenericEnumFunction<TaskStatus> {

    DRAFT, ONGOING, COMPLETED, CANCELED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
