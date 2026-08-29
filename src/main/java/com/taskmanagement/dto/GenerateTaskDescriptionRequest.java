package com.taskmanagement.dto;

import lombok.Data;

@Data
public class GenerateTaskDescriptionRequest {

    private String title;

    private String additionalDetails;
}git