package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskComment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskCommentRepository
        extends JpaRepository<TaskComment, Long> {

    List<TaskComment> findByTaskOrderByCreatedAtAsc(
            Task task
    );
}