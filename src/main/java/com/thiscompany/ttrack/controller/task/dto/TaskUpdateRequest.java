package com.thiscompany.ttrack.controller.task.dto;

import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(

        @Size(max = 200, message = "field.title_is_long")
        String title,

        @Size(max = 540, message = "field.details_are_long")
        String details,

        String priority

) {}
