package com.thiscompany.ttrack.controller.task.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

public record NewTaskRequest(

        @Nullable
        @Size(max = 300, message = "field.title_is_long")
        String title,

        @Nullable
        @Size(max = 540, message = "field.details_are_long")
        String details,

        @Nullable
        String priority

) {

}
