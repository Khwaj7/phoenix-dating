package com.phoenix.dating.repository.user;

import com.phoenix.dating.profile.model.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = @Index(name = "idx_users_email", columnList = "email", unique = true)
)
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Gender gender;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String country;

    @Column(nullable = false)
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "user_photos",
            joinColumns = @JoinColumn(name = "user_id"),
            indexes = @Index(name = "idx_user_photos_user_id", columnList = "user_id")
    )
    @Column(name = "photo_url", nullable = false, length = 2048)
    @OrderColumn(name = "position")
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

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