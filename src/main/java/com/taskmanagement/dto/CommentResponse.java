package com.taskmanagement.dto;

import com.taskmanagement.entity.TaskComment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String comment,
        Long userId,
        String userName,
        LocalDateTime createdAt
) {

    public static CommentResponse from(
            TaskComment taskComment) {

        return new CommentResponse(
                taskComment.getId(),
                taskComment.getComment(),
                taskComment.getUser().getId(),
                taskComment.getUser().getName(),
                taskComment.getCreatedAt()
        );
    }
}