package com.taskmanagement.service;

import com.taskmanagement.dto.CommentResponse;
import com.taskmanagement.dto.CreateCommentRequest;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskComment;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.TaskCommentRepository;
import com.taskmanagement.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCommentService {

    private final TaskRepository taskRepository;

    private final TaskCommentRepository
            taskCommentRepository;

    public CommentResponse addComment(
            Long taskId,
            CreateCommentRequest request,
            User loggedInUser) {

        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Task not found"
                        )
                );

        validateTaskAccess(
                task,
                loggedInUser
        );

        TaskComment taskComment =
                new TaskComment();

        taskComment.setComment(
                request.getComment()
        );

        taskComment.setTask(task);

        taskComment.setUser(
                loggedInUser
        );

        TaskComment savedComment =
                taskCommentRepository.save(
                        taskComment
                );

        return CommentResponse.from(
                savedComment
        );
    }

    public List<CommentResponse> getComments(
            Long taskId,
            User loggedInUser) {

        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Task not found"
                        )
                );

        validateTaskAccess(
                task,
                loggedInUser
        );

        return taskCommentRepository
                .findByTaskOrderByCreatedAtAsc(task)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    private void validateTaskAccess(
            Task task,
            User user) {

        boolean isCreator =
                task.getCreatedBy()
                        .getId()
                        .equals(user.getId());

        boolean isAssignee =
                task.getAssignedTo() != null
                        && task.getAssignedTo()
                        .getId()
                        .equals(user.getId());

        if (!isCreator && !isAssignee) {

            throw new RuntimeException(
                    "You are not authorized to access this task"
            );
        }
    }
}