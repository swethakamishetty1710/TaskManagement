package com.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateCommentRequest {

    @NotBlank(message = "Comment is required")
    private String comment;
}