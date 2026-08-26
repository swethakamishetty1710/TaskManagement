package com.taskmanagement.dto;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AssignTaskRequest {

    @NotNull(message = "Assigned user ID is required")
    private Long assignedToUserId;
}