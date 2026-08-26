package com.taskmanagement.dto;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private LocalDate dueDate;

    private TaskStatus status;

    private Long createdByUserId;

    private String createdByName;

    private Long assignedToUserId;

    private String assignedToName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static TaskResponse from(Task task) {

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .createdByUserId(
                        task.getCreatedBy().getId()
                )
                .createdByName(
                        task.getCreatedBy().getName()
                )
                .assignedToUserId(
                        task.getAssignedTo() != null
                                ? task.getAssignedTo().getId()
                                : null
                )
                .assignedToName(
                        task.getAssignedTo() != null
                                ? task.getAssignedTo().getName()
                                : null
                )
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}