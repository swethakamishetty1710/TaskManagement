package com.taskmanagement.service;

import com.taskmanagement.dto.CreateTaskRequest;
import com.taskmanagement.dto.TaskResponse;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public TaskResponse createTask(
            CreateTaskRequest request,
            User loggedInUser
    ) {

        User assignedUser = null;

        if (request.getAssignedToUserId() != null) {

            assignedUser =
                    userRepository
                            .findById(
                                    request.getAssignedToUserId()
                            )
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Assigned user not found"
                                    )
                            );
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .status(TaskStatus.OPEN)
                .createdBy(loggedInUser)
                .assignedTo(assignedUser)
                .build();

        Task savedTask =
                taskRepository.save(task);

        return TaskResponse.from(savedTask);
    }

    public List<TaskResponse> getMyTasks(
            User loggedInUser,
            TaskStatus status,
            String search) {

        List<Task> tasks;

        if (search != null && !search.trim().isEmpty()) {

            tasks = taskRepository
                    .searchMyTasks(
                            loggedInUser,
                            search.trim()
                    );

            if (status != null) {

                tasks = tasks
                        .stream()
                        .filter(task ->
                                task.getStatus()
                                        .equals(status))
                        .toList();
            }

        } else if (status != null) {

            tasks = taskRepository
                    .findByAssignedToAndStatus(
                            loggedInUser,
                            status
                    );

        } else {

            tasks = taskRepository
                    .findByAssignedTo(loggedInUser);
        }

        return tasks
                .stream()
                .map(TaskResponse::from)
                .toList();
    }
    public TaskResponse markTaskAsCompleted(
            Long taskId,
            User loggedInUser) {

        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Task not found"
                        )
                );

        if (task.getAssignedTo() == null
                || !task.getAssignedTo()
                .getId()
                .equals(loggedInUser.getId())) {

            throw new RuntimeException(
                    "You are not authorized to complete this task"
            );
        }

        task.setStatus(TaskStatus.COMPLETED);

        Task updatedTask =
                taskRepository.save(task);

        return TaskResponse.from(updatedTask);
    }
    public TaskResponse assignTask(
            Long taskId,
            Long assignedToUserId,
            User loggedInUser) {

        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Task not found"
                        )
                );

        User assignedUser = userRepository
                .findById(assignedToUserId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User to assign not found"
                        )
                );

        if (!task.getCreatedBy()
                .getId()
                .equals(loggedInUser.getId())) {

            throw new RuntimeException(
                    "Only the task creator can assign the task"
            );
        }

        task.setAssignedTo(assignedUser);

        Task updatedTask =
                taskRepository.save(task);

        return TaskResponse.from(updatedTask);
    }
    public TaskResponse updateTaskStatus(
            Long taskId,
            TaskStatus newStatus,
            User loggedInUser) {

        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Task not found"
                        )
                );

        boolean isCreator =
                task.getCreatedBy()
                        .getId()
                        .equals(loggedInUser.getId());

        boolean isAssignee =
                task.getAssignedTo() != null
                        && task.getAssignedTo()
                        .getId()
                        .equals(loggedInUser.getId());

        if (!isCreator && !isAssignee) {

            throw new RuntimeException(
                    "You are not authorized to update this task"
            );
        }

        validateStatusTransition(
                task.getStatus(),
                newStatus
        );

        task.setStatus(newStatus);

        Task savedTask =
                taskRepository.save(task);

        return TaskResponse.from(savedTask);
    }
    private void validateStatusTransition(
            TaskStatus currentStatus,
            TaskStatus newStatus) {

        if (currentStatus == newStatus) {
            throw new RuntimeException(
                    "Task is already in " + currentStatus + " status"
            );
        }

        if (currentStatus == TaskStatus.OPEN
                && newStatus == TaskStatus.IN_PROGRESS) {
            return;
        }

        if (currentStatus == TaskStatus.IN_PROGRESS
                && newStatus == TaskStatus.COMPLETED) {
            return;
        }

        throw new RuntimeException(
                "Invalid status transition from "
                        + currentStatus
                        + " to "
                        + newStatus
        );
    }
}