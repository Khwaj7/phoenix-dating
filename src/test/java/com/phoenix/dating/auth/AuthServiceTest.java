package com.phoenix.dating.auth;

import com.phoenix.dating.AbstractIntegrationTest;
import com.phoenix.dating.auth.dto.AuthResponse;
import com.phoenix.dating.auth.dto.LoginRequest;
import com.phoenix.dating.auth.dto.RegisterRequest;
import com.phoenix.dating.profile.model.Gender;
import com.phoenix.dating.repository.user.UserEntity;
import com.phoenix.dating.repository.user.UserRepository;
import com.phoenix.dating.repository.userPreferences.UserPreferencesRepository;
import com.phoenix.dating.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class AuthServiceTest extends AbstractIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPreferencesRepository userPreferencesRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userPreferencesRepository.deleteAll();
        userRepository.deleteAll();
    }

    private RegisterRequest registerRequest(String email) {
        return new RegisterRequest(
                email,
                "password123",
                "Test User",
                LocalDate.of(1995, 5, 10),
                Gender.MALE,
                "Paris",
                "France"
        );
    }

    @Test
    void registerPersistsUserAndReturnsValidToken() {
        AuthResponse response = authService.register(registerRequest("register@test.local"));

        UserEntity saved = userRepository.findByEmail("register@test.local").orElseThrow();
        assertThat(jwtService.isTokenValid(response.token())).isTrue();
        assertThat(jwtService.extractUserId(response.token())).isEqualTo(saved.getId());
        assertThat(saved.isEnabled()).isTrue();
    }

    @Test
    void registerHashesThePassword() {
        authService.register(registerRequest("hash@test.local"));

        UserEntity saved = userRepository.findByEmail("hash@test.local").orElseThrow();
        assertThat(saved.getPasswordHash()).isNotEqualTo("password123");
        assertThat(passwordEncoder.matches("password123", saved.getPasswordHash())).isTrue();
    }

    @Test
    void registerRejectsDuplicateEmailWithConflict() {
        authService.register(registerRequest("duplicate@test.local"));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> authService.register(registerRequest("duplicate@test.local")))
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.CONFLICT));
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void loginReturnsValidTokenForCorrectCredentials() {
        authService.register(registerRequest("login@test.local"));

        AuthResponse response = authService.login(new LoginRequest("login@test.local", "password123"));

        UserEntity saved = userRepository.findByEmail("login@test.local").orElseThrow();
        assertThat(jwtService.isTokenValid(response.token())).isTrue();
        assertThat(jwtService.extractUserId(response.token())).isEqualTo(saved.getId());
    }

    @Test
    void loginRejectsWrongPasswordWithUnauthorized() {
        authService.register(registerRequest("wrongpass@test.local"));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> authService.login(new LoginRequest("wrongpass@test.local", "not-the-password")))
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void loginRejectsUnknownEmailWithUnauthorized() {
        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> authService.login(new LoginRequest("unknown@test.local", "password123")))
                .satisfies(ex -> assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED));
    }
}
