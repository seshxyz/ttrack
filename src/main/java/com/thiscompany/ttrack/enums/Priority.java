package com.thiscompany.ttrack.enums;

public enum Priority implements GenericEnumFunction<Priority> {

    LOW, NORMAL, HIGH;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
