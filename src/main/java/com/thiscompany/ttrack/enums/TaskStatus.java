package com.thiscompany.ttrack.enums;

public enum TaskStatus {

    DRAFT(0), IN_PROGRESS(1), COMPLETED(2), CANCELED(3);

    private final int status;

    TaskStatus(int code) {
        this.status = code;
    }

}
