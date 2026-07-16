package com.phoenix.dating.repository.userPreferences;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPreferencesRepository extends JpaRepository<UserPreferencesEntity, UUID> {
}
