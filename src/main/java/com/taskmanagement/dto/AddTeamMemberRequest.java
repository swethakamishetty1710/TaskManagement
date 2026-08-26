package com.taskmanagement.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddTeamMemberRequest {

    @NotNull(message = "User ID is required")
    private Long userId;
}