package com.phoenix.dating.swipe;

import com.phoenix.dating.swipe.model.SwipeAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "swipes",
        uniqueConstraints = @UniqueConstraint(name = "uq_swipe_swiper_swiped", columnNames = {"swiper_id", "swiped_id"}),
        indexes = {
                @Index(name = "idx_swipes_swiper_id", columnList = "swiper_id"),
                @Index(name = "idx_swipes_swiped_id", columnList = "swiped_id"),
                @Index(name = "idx_swipes_swiper_swiped_action", columnList = "swiper_id, swiped_id, action")
        }
)
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Swipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "swiper_id", nullable = false, updatable = false)
    private UUID swiperId;

    @Column(name = "swiped_id", nullable = false, updatable = false)
    private UUID swipedId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SwipeAction action;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
    }
}