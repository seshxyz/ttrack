package com.thiscompany.ttrack.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewTaskRequest(

        @NotBlank(message = "Title must not be empty")
        @Size(min = 3, max = 300, message = "Title must be between 5 and 300 characters")
        String title,

        @Size(max = 540)
        String description,

        String status,

        String priority

) {
}
