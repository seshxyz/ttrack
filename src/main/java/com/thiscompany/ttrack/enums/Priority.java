package com.thiscompany.ttrack.enums;

public enum Priority {

    LOW("low"), NORMAL("normal"), HIGH("high");

    private final String name;

    Priority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
