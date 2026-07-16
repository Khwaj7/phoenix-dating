package com.phoenix.dating.auth;

import com.phoenix.dating.auth.dto.AuthResponse;
import com.phoenix.dating.auth.dto.LoginRequest;
import com.phoenix.dating.auth.dto.RegisterRequest;
import com.phoenix.dating.security.JwtService;
import com.phoenix.dating.repository.user.UserEntity;
import com.phoenix.dating.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        UserEntity userEntity = UserEntity.builder()
                .email(registerRequest.email())
                .passwordHash(passwordEncoder.encode(registerRequest.password()))
                .displayName(registerRequest.displayName())
                .birthDate(registerRequest.birthDate())
                .gender(registerRequest.gender())
                .city(registerRequest.city())
                .country(registerRequest.country())
                .enabled(true)
                .build();
        userRepository.save(userEntity);

        return new AuthResponse(jwtService.generateToken(userEntity));
    }

    public AuthResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByEmail(loginRequest.email())
                .filter(userEntity -> passwordEncoder.matches(loginRequest.password(), userEntity.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        return new AuthResponse(jwtService.generateToken(user));
    }
}
