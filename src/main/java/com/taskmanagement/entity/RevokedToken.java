package com.taskmanagement.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "revoked_tokens",
        indexes = {
                @Index(
                        name = "idx_revoked_token",
                        columnList = "token",
                        unique = true
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class RevokedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 1000
    )
    private String token;

    @Column(nullable = false)
    private LocalDateTime revokedAt;

    public RevokedToken(String token) {
        this.token = token;
        this.revokedAt = LocalDateTime.now();
    }
}