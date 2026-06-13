package com.phoenix.dating.profile;

import com.phoenix.dating.profile.dto.ProfileResponse;
import com.phoenix.dating.profile.model.Gender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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
            ),
            "550e8400-e29b-41d4-a716-446655440003", new ProfileResponse(
                    "550e8400-e29b-41d4-a716-446655440003",
                    "David",
                    "Cinéphile et cuisinier amateur",
                    LocalDate.of(1991, 11, 30),
                    Gender.MALE,
                    "Lille",
                    "France",
                    List.of("https://randomuser.me/api/portraits/men/4.jpg")
            ),
            "550e8400-e29b-41d4-a716-446655440004", new ProfileResponse(
                    "550e8400-e29b-41d4-a716-446655440004",
                    "Emma",
                    "Toujours partante pour un concert",
                    LocalDate.of(1996, 6, 18),
                    Gender.FEMALE,
                    "Marseille",
                    "France",
                    List.of("https://randomuser.me/api/portraits/women/5.jpg")
            ),
            "550e8400-e29b-41d4-a716-446655440005", new ProfileResponse(
                    "550e8400-e29b-41d4-a716-446655440005",
                    "Farid",
                    "Grimpeur, lecteur de SF et fan de jeux de société",
                    LocalDate.of(1994, 2, 9),
                    Gender.MALE,
                    "Toulouse",
                    "France",
                    List.of("https://randomuser.me/api/portraits/men/6.jpg")
            ),
            "550e8400-e29b-41d4-a716-446655440006", new ProfileResponse(
                    "550e8400-e29b-41d4-a716-446655440006",
                    "Garance",
                    "Photographe en quête de lumière",
                    LocalDate.of(1997, 9, 27),
                    Gender.FEMALE,
                    "Nantes",
                    "France",
                    List.of("https://randomuser.me/api/portraits/women/7.jpg")
            )
    );

    public ProfileResponse getMe() {
        return MOCK_PROFILES.get(MOCK_ME_ID);
    }

    public Optional<ProfileResponse> getById(String userId) {
        return Optional.ofNullable(MOCK_PROFILES.get(userId));
    }

    public List<ProfileResponse> getDiscoveryFeed(int limit) {
        List<ProfileResponse> candidates = new ArrayList<>(MOCK_PROFILES.values());
        candidates.removeIf(profile -> MOCK_ME_ID.equals(profile.id()));
        Collections.shuffle(candidates);
        return candidates.stream()
                .limit(Math.max(limit, 0))
                .toList();
    }
}
