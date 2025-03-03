package com.thiscompany.ttrack.controller.payload;

public record NewTaskRequest(
        String title,
        String description,
        String status
) {
}
