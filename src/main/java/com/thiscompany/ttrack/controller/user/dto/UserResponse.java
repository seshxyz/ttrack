package com.thiscompany.ttrack.controller.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.thiscompany.ttrack.model.view.DtosViews;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(

        String id,

        @JsonView(DtosViews.UserBaseView.class)
        String username,

        boolean isActive,

        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime lastLogin,

        Set<String> permissions

) {
}
