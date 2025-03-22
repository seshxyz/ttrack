package com.thiscompany.ttrack.enums;

public enum TaskState {

    INITIAL("initial"), ACTIVE("active"), EXPIRED("expired");

    private final String name;

    TaskState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
