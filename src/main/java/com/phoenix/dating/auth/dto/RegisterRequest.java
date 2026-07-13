package com.phoenix.dating.auth.dto;

import com.phoenix.dating.profile.model.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Size(max = 100) String displayName,
        @NotNull @Past LocalDate birthDate,
        @NotNull Gender gender,
        @Size(max = 100) String city,
        @Size(max = 100) String country
) {
}
