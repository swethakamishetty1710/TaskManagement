package com.taskmanagement.service;

import com.taskmanagement.dto.AttachmentResponse;
import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskAttachment;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.TaskAttachmentRepository;
import com.taskmanagement.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskAttachmentService {

    private final TaskRepository taskRepository;

    private final TaskAttachmentRepository
            taskAttachmentRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AttachmentResponse uploadAttachment(
            Long taskId,
            MultipartFile file,
            User loggedInUser) throws IOException {

        if (file == null || file.isEmpty()) {

            throw new RuntimeException(
                    "File is required"
            );
        }

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

        Path uploadPath =
                Paths.get(uploadDir)
                        .toAbsolutePath()
                        .normalize();

        Files.createDirectories(uploadPath);

        String originalFileName =
                file.getOriginalFilename();

        String storedFileName =
                UUID.randomUUID()
                        + "_"
                        + originalFileName;

        Path filePath =
                uploadPath.resolve(storedFileName);

        file.transferTo(filePath.toFile());

        TaskAttachment attachment =
                new TaskAttachment();

        attachment.setFileName(
                originalFileName
        );

        attachment.setFileType(
                file.getContentType()
        );

        attachment.setFileSize(
                file.getSize()
        );

        attachment.setFilePath(
                filePath.toString()
        );

        attachment.setTask(task);

        attachment.setUploadedBy(
                loggedInUser
        );

        TaskAttachment savedAttachment =
                taskAttachmentRepository.save(
                        attachment
                );

        return AttachmentResponse.from(
                savedAttachment
        );
    }

    public List<AttachmentResponse> getAttachments(
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

        return taskAttachmentRepository
                .findByTaskOrderByCreatedAtAsc(task)
                .stream()
                .map(AttachmentResponse::from)
                .toList();
    }

    public Resource downloadAttachment(
            Long attachmentId,
            User loggedInUser) throws IOException {

        TaskAttachment attachment =
                taskAttachmentRepository
                        .findById(attachmentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Attachment not found"
                                )
                        );

        validateTaskAccess(
                attachment.getTask(),
                loggedInUser
        );

        Path path =
                Paths.get(
                        attachment.getFilePath()
                );

        Resource resource =
                new UrlResource(
                        path.toUri()
                );

        if (!resource.exists()
                || !resource.isReadable()) {

            throw new RuntimeException(
                    "File not found"
            );
        }

        return resource;
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