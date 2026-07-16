package com.phoenix.dating.profile;

import com.phoenix.dating.profile.dto.OnboardingPreferencesRequest;
import com.phoenix.dating.profile.dto.ProfileResponse;
import com.phoenix.dating.repository.user.UserEntity;
import com.phoenix.dating.repository.user.UserRepository;
import com.phoenix.dating.repository.userPreferences.UserPreferencesEntity;
import com.phoenix.dating.repository.userPreferences.UserPreferencesRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final UserPreferencesRepository userPreferencesRepository;

    ProfileService(UserRepository userRepository, UserPreferencesRepository userPreferencesRepository) {
        this.userRepository = userRepository;
        this.userPreferencesRepository = userPreferencesRepository;
    }

    ProfileResponse getMe(UUID currentUserId) {
        return userRepository.findById(currentUserId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    ProfileResponse getById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    List<ProfileResponse> getDiscoveryFeed(UUID currentUserId, int limit) {
        List<UserEntity> candidates = userRepository.findRandomExcluding(currentUserId, PageRequest.of(0, Math.max(limit, 1)));

        return candidates.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    void onboardUser(UserEntity currentUser, OnboardingPreferencesRequest request) {
        UserPreferencesEntity userPreferences = userPreferencesRepository.findById(currentUser.getId())
                .orElseGet(() -> UserPreferencesEntity.builder()
                        .user(userRepository.getReferenceById(currentUser.getId()))
                        .build());

        userPreferences.setSeekingGender(request.seekingGender());
        userPreferences.setSeekingRelationshipGoal(request.seekingRelationshipGoal());
        userPreferences.setSeekingAgeRange(request.seekingAgeRange());
        userPreferences.setSeekingDistance(request.seekingDistance());
        userPreferences.setSeekingAttractionFocus(request.seekingAttractionFocus());

        userPreferencesRepository.save(userPreferences);
    }

    private ProfileResponse toResponse(UserEntity user) {
        return new ProfileResponse(
                user.getId().toString(),
                user.getDisplayName(),
                user.getBio(),
                user.getBirthDate(),
                user.getGender(),
                user.getCity(),
                user.getCountry(),
                user.getPhotoUrls()
        );
    }
}
