package com.taskmanagement.controller;

import com.taskmanagement.dto.AttachmentResponse;
import com.taskmanagement.entity.TaskAttachment;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.TaskAttachmentRepository;
import com.taskmanagement.service.TaskAttachmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskAttachmentController {

    private final TaskAttachmentService
            taskAttachmentService;

    private final TaskAttachmentRepository
            taskAttachmentRepository;

    @PostMapping(
            value = "/tasks/{taskId}/attachments",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AttachmentResponse> uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication)
            throws IOException {

        User loggedInUser =
                (User) authentication.getPrincipal();

        AttachmentResponse response =
                taskAttachmentService.uploadAttachment(
                        taskId,
                        file,
                        loggedInUser
                );

        return ResponseEntity
                .status(201)
                .body(response);
    }

    @GetMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<List<AttachmentResponse>>
    getAttachments(
            @PathVariable Long taskId,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        List<AttachmentResponse> attachments =
                taskAttachmentService.getAttachments(
                        taskId,
                        loggedInUser
                );

        return ResponseEntity.ok(
                attachments
        );
    }

    @GetMapping(
            "/attachments/{attachmentId}/download"
    )
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long attachmentId,
            Authentication authentication)
            throws IOException {

        User loggedInUser =
                (User) authentication.getPrincipal();

        Resource resource =
                taskAttachmentService.downloadAttachment(
                        attachmentId,
                        loggedInUser
                );

        TaskAttachment attachment =
                taskAttachmentRepository
                        .findById(attachmentId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Attachment not found"
                                )
                        );

        String contentType =
                attachment.getFileType();

        if (contentType == null) {
            contentType =
                    MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                contentType
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + attachment.getFileName()
                                + "\""
                )
                .body(resource);
    }
}