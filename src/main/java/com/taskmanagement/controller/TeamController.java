package com.taskmanagement.controller;

import com.taskmanagement.dto.AddTeamMemberRequest;
import com.taskmanagement.dto.CreateTeamRequest;
import com.taskmanagement.dto.TeamMemberResponse;
import com.taskmanagement.dto.TeamResponse;
import com.taskmanagement.entity.User;
import com.taskmanagement.service.TeamService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(
            @Valid @RequestBody CreateTeamRequest request,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        TeamResponse response =
                teamService.createTeam(
                        request,
                        loggedInUser
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getMyTeams(
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                teamService.getMyTeams(
                        loggedInUser
                )
        );
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(
            @PathVariable Long teamId,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                teamService.getTeam(
                        teamId,
                        loggedInUser
                )
        );
    }
    @PostMapping("/{teamId}/members")
    public ResponseEntity<TeamMemberResponse> addMember(
            @PathVariable Long teamId,
            @Valid @RequestBody AddTeamMemberRequest request,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        TeamMemberResponse response =
                teamService.addMember(
                        teamId,
                        request.getUserId(),
                        loggedInUser
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<TeamMemberResponse>>
    getMembers(
            @PathVariable Long teamId,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                teamService.getMembers(
                        teamId,
                        loggedInUser
                )
        );
    }
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            Authentication authentication) {

        User loggedInUser =
                (User) authentication.getPrincipal();

        teamService.removeMember(
                teamId,
                userId,
                loggedInUser
        );

        return ResponseEntity.noContent()
                .build();
    }
}