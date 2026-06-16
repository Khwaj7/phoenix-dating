package com.phoenix.dating.swipe.match;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "matches",
        uniqueConstraints = @UniqueConstraint(name = "uq_match_users", columnNames = {"user1_id", "user2_id"}),
        indexes = {
                @Index(name = "idx_matches_user1_id", columnList = "user1_id"),
                @Index(name = "idx_matches_user2_id", columnList = "user2_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user1_id", nullable = false, updatable = false)
    private UUID user1Id;

    @Column(name = "user2_id", nullable = false, updatable = false)
    private UUID user2Id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
    }
}