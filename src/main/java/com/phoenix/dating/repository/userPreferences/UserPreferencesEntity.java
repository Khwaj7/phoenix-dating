package com.phoenix.dating.repository.userPreferences;

import com.phoenix.dating.profile.model.SeekingAgeRange;
import com.phoenix.dating.profile.model.SeekingAttractionFocus;
import com.phoenix.dating.profile.model.SeekingDistance;
import com.phoenix.dating.profile.model.SeekingGender;
import com.phoenix.dating.profile.model.SeekingRelationshipGoal;
import com.phoenix.dating.repository.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserPreferencesEntity {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "seeking_gender", nullable = false, length = 20)
    private SeekingGender seekingGender;

    @Enumerated(EnumType.STRING)
    @Column(name = "seeking_relationship_goal", nullable = false, length = 20)
    private SeekingRelationshipGoal seekingRelationshipGoal;

    @Enumerated(EnumType.STRING)
    @Column(name = "seeking_age_range", nullable = false, length = 20)
    private SeekingAgeRange seekingAgeRange;

    @Enumerated(EnumType.STRING)
    @Column(name = "seeking_distance", nullable = false, length = 20)
    private SeekingDistance seekingDistance;

    @Enumerated(EnumType.STRING)
    @Column(name = "seeking_attraction_focus", nullable = false, length = 20)
    private SeekingAttractionFocus seekingAttractionFocus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }
}
