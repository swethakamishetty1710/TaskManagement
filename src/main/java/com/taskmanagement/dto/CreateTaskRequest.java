package com.taskmanagement.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @FutureOrPresent(
            message = "Due date cannot be in the past"
    )
    private LocalDate dueDate;

    private Long assignedToUserId;
}