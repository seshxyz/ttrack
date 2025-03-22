package com.thiscompany.ttrack.controller.payload;

import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(

        @Size(max = 200, message = "Title is too long or short")
        String title,

        @Size(max = 540, message = "Description is too long")
        String description,

        String priority

) {}
