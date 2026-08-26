package com.taskmanagement.service;

import com.taskmanagement.dto.AddTeamMemberRequest;
import com.taskmanagement.dto.CreateTeamRequest;
import com.taskmanagement.dto.TeamMemberResponse;
import com.taskmanagement.dto.TeamResponse;
import com.taskmanagement.entity.Team;
import com.taskmanagement.entity.TeamMember;
import com.taskmanagement.entity.User;
import com.taskmanagement.repository.TeamMemberRepository;
import com.taskmanagement.repository.TeamRepository;
import com.taskmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamMemberRepository teamMemberRepository;

    private final UserRepository userRepository;


    // =========================
    // CREATE TEAM
    // =========================

    public TeamResponse createTeam(
            CreateTeamRequest request,
            User loggedInUser) {

        if (teamRepository
                .findByName(request.getName())
                .isPresent()) {

            throw new RuntimeException(
                    "Team with this name already exists"
            );
        }

        Team team = new Team();

        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setCreatedBy(loggedInUser);

        Team savedTeam =
                teamRepository.save(team);

        return TeamResponse.from(savedTeam);
    }


    // =========================
    // GET MY TEAMS
    // =========================

    public List<TeamResponse> getMyTeams(
            User loggedInUser) {

        return teamRepository
                .findByCreatedBy(loggedInUser)
                .stream()
                .map(TeamResponse::from)
                .toList();
    }


    // =========================
    // GET TEAM
    // =========================

    public TeamResponse getTeam(
            Long teamId,
            User loggedInUser) {

        Team team =
                teamRepository
                        .findById(teamId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Team not found"
                                )
                        );

        validateTeamOwner(
                team,
                loggedInUser
        );

        return TeamResponse.from(team);
    }


    // =========================
    // ADD TEAM MEMBER
    // =========================

    public TeamMemberResponse addMember(
            Long teamId,
            Long userId,
            User loggedInUser) {

        Team team =
                teamRepository
                        .findById(teamId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Team not found"
                                )
                        );

        validateTeamOwner(
                team,
                loggedInUser
        );

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        if (team.getCreatedBy()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "Team owner is already a team member"
            );
        }

        if (teamMemberRepository
                .existsByTeamAndUser(team, user)) {

            throw new RuntimeException(
                    "User is already a member of this team"
            );
        }

        TeamMember teamMember =
                new TeamMember();

        teamMember.setTeam(team);
        teamMember.setUser(user);

        TeamMember savedMember =
                teamMemberRepository.save(
                        teamMember
                );

        return TeamMemberResponse.from(
                savedMember
        );
    }


    // =========================
    // GET TEAM MEMBERS
    // =========================

    public List<TeamMemberResponse> getMembers(
            Long teamId,
            User loggedInUser) {

        Team team =
                teamRepository
                        .findById(teamId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Team not found"
                                )
                        );

        validateTeamOwner(
                team,
                loggedInUser
        );

        return teamMemberRepository
                .findByTeam(team)
                .stream()
                .map(TeamMemberResponse::from)
                .toList();
    }


    // =========================
    // REMOVE TEAM MEMBER
    // =========================

    public void removeMember(
            Long teamId,
            Long userId,
            User loggedInUser) {

        Team team =
                teamRepository
                        .findById(teamId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Team not found"
                                )
                        );

        validateTeamOwner(
                team,
                loggedInUser
        );

        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );

        TeamMember teamMember =
                teamMemberRepository
                        .findByTeamAndUser(
                                team,
                                user
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User is not a member of this team"
                                )
                        );

        teamMemberRepository.delete(
                teamMember
        );
    }


    // =========================
    // TEAM OWNER VALIDATION
    // =========================

    private void validateTeamOwner(
            Team team,
            User loggedInUser) {

        if (!team.getCreatedBy()
                .getId()
                .equals(loggedInUser.getId())) {

            throw new RuntimeException(
                    "Only the team owner can manage team members"
            );
        }
    }
}