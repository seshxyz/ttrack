package com.thiscompany.ttrack.controller.payload;

public record TaskResponse(

        Long id,
        String title,
        String description,
        String priority,
        String status

) {
}
