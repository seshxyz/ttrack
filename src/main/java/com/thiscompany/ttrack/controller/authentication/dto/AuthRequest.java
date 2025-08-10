package com.thiscompany.ttrack.controller.authentication.dto;

import jakarta.validation.constraints.NotEmpty;

public record AuthRequest(
        @NotEmpty(message = "field.username.is_empty")
        String username,

        @NotEmpty(message = "field.password.is_empty")
        String password
) {}
