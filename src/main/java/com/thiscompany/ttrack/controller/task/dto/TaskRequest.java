package com.thiscompany.ttrack.controller.task.dto;

import jakarta.validation.constraints.NotEmpty;


public record TaskRequest(

        @NotEmpty(message = "field.id.is_empty")
        String id

){}
