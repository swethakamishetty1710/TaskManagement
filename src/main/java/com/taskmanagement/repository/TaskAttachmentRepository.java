package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskAttachment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskAttachmentRepository
        extends JpaRepository<TaskAttachment, Long> {

    List<TaskAttachment> findByTaskOrderByCreatedAtAsc(
            Task task
    );
}