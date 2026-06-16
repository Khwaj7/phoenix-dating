package com.phoenix.dating.profile;

import com.phoenix.dating.profile.dto.ProfileResponse;
import com.phoenix.dating.user.UserEntity;
import com.phoenix.dating.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ProfileService {
    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ProfileResponse getMe(UUID currentUserId) {
        return userRepository.findById(currentUserId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

    public ProfileResponse getById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<ProfileResponse> getDiscoveryFeed(int limit) {
        UUID currentUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        List<UserEntity> candidates = userRepository.findRandomExcluding(currentUserId, PageRequest.of(0, Math.max(limit, 0)));

        return candidates.stream()
                .map(this::toResponse)
                .toList();
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
