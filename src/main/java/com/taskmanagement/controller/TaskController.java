package com.taskmanagement.controller;

import com.taskmanagement.dto.AssignTaskRequest;
import com.taskmanagement.dto.CreateTaskRequest;
import com.taskmanagement.dto.TaskResponse;
import com.taskmanagement.dto.UpdateTaskStatusRequest;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.entity.User;
import com.taskmanagement.service.TaskService;

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
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        TaskResponse response =
                taskService.createTask(
                        request,
                        loggedInUser
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<TaskResponse>> getMyTasks(
            @RequestParam(required = false)
            TaskStatus status,
            @RequestParam(required = false)
            String search,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        List<TaskResponse> tasks =
                taskService.getMyTasks(
                        loggedInUser,
                        status,
                        search
                );

        return ResponseEntity.ok(tasks);
    }
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<TaskResponse> markTaskAsCompleted(
            @PathVariable Long taskId,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        TaskResponse response =
                taskService.markTaskAsCompleted(
                        taskId,
                        loggedInUser
                );

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable Long taskId,
            @Valid @RequestBody AssignTaskRequest request,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        TaskResponse response =

                taskService.assignTask(
                        taskId,
                        request.getAssignedToUserId(),
                        loggedInUser
                );

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        TaskResponse response =
                taskService.updateTaskStatus(
                        taskId,
                        request.getStatus(),
                        loggedInUser
                );

        return ResponseEntity.ok(response);
    }
}