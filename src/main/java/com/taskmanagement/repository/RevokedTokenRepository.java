package com.taskmanagement.repository;

import com.taskmanagement.entity.RevokedToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenRepository
        extends JpaRepository<RevokedToken, Long> {

    boolean existsByToken(String token);
}