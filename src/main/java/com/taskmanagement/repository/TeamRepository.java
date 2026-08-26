package com.taskmanagement.repository;

import com.taskmanagement.entity.Team;
import com.taskmanagement.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository
        extends JpaRepository<Team, Long> {

    Optional<Team> findByName(String name);

    List<Team> findByCreatedBy(User user);
}