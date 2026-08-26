package com.taskmanagement.dto;

import com.taskmanagement.entity.TeamMember;

import java.time.LocalDateTime;

public record TeamMemberResponse(
        Long userId,
        String name,
        String email,
        LocalDateTime joinedAt
) {

    public static TeamMemberResponse from(
            TeamMember teamMember) {

        return new TeamMemberResponse(
                teamMember.getUser().getId(),
                teamMember.getUser().getName(),
                teamMember.getUser().getEmail(),
                teamMember.getJoinedAt()
        );
    }
}