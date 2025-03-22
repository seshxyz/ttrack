package com.thiscompany.ttrack.enums;

public enum TaskStatus {

    DRAFT("draft"), IN_PROGRESS("in progress"), COMPLETED("completed"), CANCELED("canceled");

    private final String name;

    TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
