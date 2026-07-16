package com.phoenix.dating.security;

import com.phoenix.dating.profile.model.Gender;
import com.phoenix.dating.repository.user.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "test-secret-test-secret-test-secret-test";
    private static final long ONE_HOUR_MS = 3_600_000L;

    private final JwtService jwtService = new JwtService(SECRET, ONE_HOUR_MS);

    private UserEntity user(UUID id) {
        return UserEntity.builder()
                .id(id)
                .email("jwt@test.local")
                .displayName("Jwt User")
                .birthDate(LocalDate.of(1995, 5, 10))
                .gender(Gender.MALE)
                .enabled(true)
                .build();
    }

    @Test
    void generatedTokenIsValidAndCarriesUserId() {
        UUID userId = UUID.randomUUID();

        String token = jwtService.generateToken(user(userId));

        assertThat(jwtService.isTokenValid(token)).isTrue();
        assertThat(jwtService.extractUserId(token)).isEqualTo(userId);
    }

    @Test
    void malformedTokenIsInvalid() {
        assertThat(jwtService.isTokenValid("not-a-jwt")).isFalse();
        assertThat(jwtService.isTokenValid("")).isFalse();
    }

    @Test
    void tokenSignedWithAnotherKeyIsInvalid() {
        JwtService otherService = new JwtService("another-secret-another-secret-another-sec", ONE_HOUR_MS);
        String token = otherService.generateToken(user(UUID.randomUUID()));

        assertThat(jwtService.isTokenValid(token)).isFalse();
    }

    @Test
    void expiredTokenIsInvalid() {
        JwtService expiredService = new JwtService(SECRET, -1_000L);
        String token = expiredService.generateToken(user(UUID.randomUUID()));

        assertThat(expiredService.isTokenValid(token)).isFalse();
    }
}
