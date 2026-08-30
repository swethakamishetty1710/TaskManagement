package com.taskmanagement.controller;

import com.taskmanagement.dto.GenerateTaskDescriptionRequest;
import com.taskmanagement.dto.GenerateTaskDescriptionResponse;
import com.taskmanagement.service.AiService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;


    @PostMapping("/generate-task-description")
    public ResponseEntity<
            GenerateTaskDescriptionResponse
            > generateTaskDescription(
            @RequestBody
            GenerateTaskDescriptionRequest request) {


        String description =
                aiService.generateTaskDescription(
                        request.getTitle(),
                        request.getAdditionalDetails()
                );


        GenerateTaskDescriptionResponse response =
                new GenerateTaskDescriptionResponse(
                        request.getTitle(),
                        description
                );


        return ResponseEntity.ok(response);
    }
}