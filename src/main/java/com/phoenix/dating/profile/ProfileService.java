package com.phoenix.dating.profile;

import com.phoenix.dating.profile.dto.ProfileResponse;
import com.phoenix.dating.profile.model.Gender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileService {

    static final String MOCK_ME_ID = "550e8400-e29b-41d4-a716-446655440000";

    private static final Map<String, ProfileResponse> MOCK_PROFILES = Map.of(
            MOCK_ME_ID, new ProfileResponse(
                    MOCK_ME_ID,
                    "Alice",
                    "Passionnée de voyages et de cuisine",
                    LocalDate.of(1995, 4, 12),
                    Gender.FEMALE,
                    "Paris",
                    "France",
                    List.of("https://randomuser.me/api/portraits/women/1.jpg")
            ),
            "550e8400-e29b-41d4-a716-446655440001", new ProfileResponse(
                    "550e8400-e29b-41d4-a716-446655440001",
                    "Bob",
                    "Développeur le jour, guitariste la nuit",
                    LocalDate.of(1993, 7, 23),
                    Gender.MALE,
                    "Lyon",
                    "France",
                    List.of("https://randomuser.me/api/portraits/men/2.jpg")
            ),
            "550e8400-e29b-41d4-a716-446655440002", new ProfileResponse(
                    "550e8400-e29b-41d4-a716-446655440002",
                    "Camille",
                    "Randonneuse et amatrice de café",
                    LocalDate.of(1998, 1, 5),
                    Gender.NON_BINARY,
                    "Bordeaux",
                    "France",
                    List.of("https://randomuser.me/api/portraits/women/3.jpg")
            )
    );

    public ProfileResponse getMe() {
        return MOCK_PROFILES.get(MOCK_ME_ID);
    }

    public Optional<ProfileResponse> getById(String userId) {
        return Optional.ofNullable(MOCK_PROFILES.get(userId));
    }
}
