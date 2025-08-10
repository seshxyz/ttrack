package com.thiscompany.ttrack.controller.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreationRequest (

        @NotEmpty
        @Size(min = 6, max = 36)
        @Pattern(regexp = "^[a-zA-Z0-9_]+$")
        String username,

        @NotEmpty
        @Size(min = 8, max = 40)
        String password

) {}
