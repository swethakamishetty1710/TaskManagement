package com.taskmanagement.repository;

import com.taskmanagement.entity.Task;
import com.taskmanagement.entity.TaskStatus;
import com.taskmanagement.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedTo(User user);

    List<Task> findByAssignedToAndStatus(
            User user,
            TaskStatus status
    );

    List<Task> findByAssignedToAndTitleContainingIgnoreCase(
            User user,
            String title
    );

    List<Task> findByAssignedToAndDescriptionContainingIgnoreCase(
            User user,
            String description
    );
    @Query("""
            SELECT t
            FROM Task t
            WHERE t.assignedTo = :user
            AND (
                LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%'))
                OR
                LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            """)
    List<Task> searchMyTasks(
            @Param("user") User user,
            @Param("search") String search
    );
}