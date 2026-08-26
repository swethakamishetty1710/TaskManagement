package com.taskmanagement.repository;

import com.taskmanagement.entity.Team;
import com.taskmanagement.entity.TeamMember;
import com.taskmanagement.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMemberRepository
        extends JpaRepository<TeamMember, Long> {

    boolean existsByTeamAndUser(
            Team team,
            User user
    );

    List<TeamMember> findByTeam(
            Team team
    );

    Optional<TeamMember> findByTeamAndUser(
            Team team,
            User user
    );

    void deleteByTeamAndUser(
            Team team,
            User user
    );
}