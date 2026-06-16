package com.phoenix.dating.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.id <> :excludeId AND u.enabled = true ORDER BY RANDOM()")
    List<UserEntity> findRandomExcluding(@Param("excludeId") UUID excludeId, Pageable pageable);
}
