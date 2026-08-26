package com.taskmanagement.dto;

import com.taskmanagement.entity.TaskStatus;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdateTaskStatusRequest {

    @NotNull(message = "Status is required")
    private TaskStatus status;
}