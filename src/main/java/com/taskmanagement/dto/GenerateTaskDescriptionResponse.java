package com.taskmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerateTaskDescriptionResponse {

    private String title;

    private String generatedDescription;
}