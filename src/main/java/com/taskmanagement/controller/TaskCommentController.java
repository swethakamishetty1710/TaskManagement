package com.taskmanagement.controller;

import com.taskmanagement.dto.CommentResponse;
import com.taskmanagement.dto.CreateCommentRequest;
import com.taskmanagement.entity.User;
import com.taskmanagement.service.TaskCommentService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CreateCommentRequest request,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        CommentResponse response =
                taskCommentService.addComment(
                        taskId,
                        request,
                        loggedInUser
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long taskId,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        List<CommentResponse> comments =
                taskCommentService.getComments(
                        taskId,
                        loggedInUser
                );

        return ResponseEntity.ok(comments);
    }
}