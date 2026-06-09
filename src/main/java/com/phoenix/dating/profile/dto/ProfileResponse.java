package com.phoenix.dating.profile.dto;

import com.phoenix.dating.profile.model.Gender;

import java.time.LocalDate;
import java.util.List;

public record ProfileResponse(
        String id,
        String displayName,
        String bio,
        LocalDate birthDate,
        Gender gender,
        String city,
        String country,
        List<String> photoUrls
) {}
