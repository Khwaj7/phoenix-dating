package com.phoenix.dating.profile;

import com.phoenix.dating.AbstractIntegrationTest;
import com.phoenix.dating.profile.dto.OnboardingPreferencesRequest;
import com.phoenix.dating.profile.dto.ProfileResponse;
import com.phoenix.dating.profile.model.Gender;
import com.phoenix.dating.profile.model.SeekingAgeRange;
import com.phoenix.dating.profile.model.SeekingAttractionFocus;
import com.phoenix.dating.profile.model.SeekingDistance;
import com.phoenix.dating.profile.model.SeekingGender;
import com.phoenix.dating.profile.model.SeekingRelationshipGoal;
import com.phoenix.dating.repository.user.UserEntity;
import com.phoenix.dating.repository.user.UserRepository;
import com.phoenix.dating.repository.userPreferences.UserPreferencesEntity;
import com.phoenix.dating.repository.userPreferences.UserPreferencesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ProfileServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @BeforeEach
    void cleanDatabase() {
        userPreferencesRepository.deleteAll();
        userRepository.deleteAll();
    }

    private UserEntity createUser(String email, boolean enabled) {
        return userRepository.save(UserEntity.builder()
                .email(email)
                .passwordHash("hash")
                .displayName("User " + email)
                .birthDate(LocalDate.of(1995, 5, 10))
                .gender(Gender.FEMALE)
                .city("Paris")
                .country("France")
                .enabled(enabled)
                .build());
    }

    private OnboardingPreferencesRequest onboardingRequest() {
        return new OnboardingPreferencesRequest(
                SeekingGender.FEMALE,
                SeekingRelationshipGoal.LONG,
                SeekingAgeRange.CLOSE,
                SeekingDistance.SAME_CITY,
                SeekingAttractionFocus.PERSONALITY
        );
    }

    @Test
    void getMeReturnsProfileOfCurrentUser() {
        UserEntity user = createUser("me@test.local", true);

        ProfileResponse response = profileService.getMe(user.getId());

        assertThat(response.id()).isEqualTo(user.getId().toString());
        assertThat(response.displayName()).isEqualTo(user.getDisplayName());
    }

    @Test
    void getMeThrowsNotFoundForUnknownUser() {
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> profileService.getMe(UUID.randomUUID()))
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void getByIdReturnsProfile() {
        UserEntity user = createUser("target@test.local", true);

        ProfileResponse response = profileService.getById(user.getId());

        assertThat(response.id()).isEqualTo(user.getId().toString());
    }

    @Test
    void getByIdThrowsNotFoundForUnknownUser() {
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> profileService.getById(UUID.randomUUID()))
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void getDiscoveryFeedExcludesCurrentUser() {
        UserEntity me = createUser("me@test.local", true);
        createUser("candidate1@test.local", true);
        createUser("candidate2@test.local", true);

        List<ProfileResponse> feed = profileService.getDiscoveryFeed(me.getId(), 10);

        assertThat(feed).hasSize(2);
        assertThat(feed).noneMatch(p -> p.id().equals(me.getId().toString()));
    }

    @Test
    void getDiscoveryFeedExcludesDisabledUsers() {
        UserEntity me = createUser("me@test.local", true);
        createUser("enabled@test.local", true);
        UserEntity disabled = createUser("disabled@test.local", false);

        List<ProfileResponse> feed = profileService.getDiscoveryFeed(me.getId(), 10);

        assertThat(feed).hasSize(1);
        assertThat(feed).noneMatch(p -> p.id().equals(disabled.getId().toString()));
    }

    @Test
    void getDiscoveryFeedRespectsLimit() {
        UserEntity me = createUser("me@test.local", true);
        for (int i = 0; i < 5; i++) {
            createUser("candidate" + i + "@test.local", true);
        }

        List<ProfileResponse> feed = profileService.getDiscoveryFeed(me.getId(), 3);

        assertThat(feed).hasSize(3);
    }

    @Test
    void getDiscoveryFeedClampsNonPositiveLimitToOne() {
        UserEntity me = createUser("me@test.local", true);
        createUser("candidate1@test.local", true);
        createUser("candidate2@test.local", true);

        List<ProfileResponse> feed = profileService.getDiscoveryFeed(me.getId(), 0);

        assertThat(feed).hasSize(1);
    }

    @Test
    void onboardUserInsertsPreferences() {
        UserEntity user = createUser("onboard@test.local", true);

        profileService.onboardUser(user, onboardingRequest());

        UserPreferencesEntity saved = userPreferencesRepository.findById(user.getId()).orElseThrow();
        assertThat(saved.getSeekingGender()).isEqualTo(SeekingGender.FEMALE);
        assertThat(saved.getSeekingRelationshipGoal()).isEqualTo(SeekingRelationshipGoal.LONG);
        assertThat(saved.getSeekingAgeRange()).isEqualTo(SeekingAgeRange.CLOSE);
        assertThat(saved.getSeekingDistance()).isEqualTo(SeekingDistance.SAME_CITY);
        assertThat(saved.getSeekingAttractionFocus()).isEqualTo(SeekingAttractionFocus.PERSONALITY);
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void onboardUserUpdatesExistingPreferencesWithoutDuplicating() {
        UserEntity user = createUser("reonboard@test.local", true);
        profileService.onboardUser(user, onboardingRequest());

        profileService.onboardUser(user, new OnboardingPreferencesRequest(
                SeekingGender.ANY,
                SeekingRelationshipGoal.SHORT,
                SeekingAgeRange.ANY,
                SeekingDistance.NEARBY,
                SeekingAttractionFocus.HUMOR
        ));

        assertThat(userPreferencesRepository.count()).isEqualTo(1);
        UserPreferencesEntity saved = userPreferencesRepository.findById(user.getId()).orElseThrow();
        assertThat(saved.getSeekingGender()).isEqualTo(SeekingGender.ANY);
        assertThat(saved.getSeekingRelationshipGoal()).isEqualTo(SeekingRelationshipGoal.SHORT);
        assertThat(saved.getSeekingAgeRange()).isEqualTo(SeekingAgeRange.ANY);
        assertThat(saved.getSeekingDistance()).isEqualTo(SeekingDistance.NEARBY);
        assertThat(saved.getSeekingAttractionFocus()).isEqualTo(SeekingAttractionFocus.HUMOR);
    }
}
