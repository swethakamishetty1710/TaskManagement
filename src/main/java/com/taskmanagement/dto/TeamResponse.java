package com.taskmanagement.dto;

import com.taskmanagement.entity.Team;

import java.time.LocalDateTime;

public record TeamResponse(
        Long id,
        String name,
        String description,
        Long createdByUserId,
        String createdByName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static TeamResponse from(Team team) {

        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getCreatedBy().getId(),
                team.getCreatedBy().getName(),
                team.getCreatedAt(),
                team.getUpdatedAt()
        );
    }
}