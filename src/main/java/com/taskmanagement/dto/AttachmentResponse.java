package com.taskmanagement.dto;

import com.taskmanagement.entity.TaskAttachment;

import java.time.LocalDateTime;

public record AttachmentResponse(
        Long id,
        String fileName,
        String fileType,
        Long fileSize,
        Long uploadedByUserId,
        String uploadedByName,
        LocalDateTime createdAt
) {

    public static AttachmentResponse from(
            TaskAttachment attachment) {

        return new AttachmentResponse(
                attachment.getId(),
                attachment.getFileName(),
                attachment.getFileType(),
                attachment.getFileSize(),
                attachment.getUploadedBy().getId(),
                attachment.getUploadedBy().getName(),
                attachment.getCreatedAt()
        );
    }
}