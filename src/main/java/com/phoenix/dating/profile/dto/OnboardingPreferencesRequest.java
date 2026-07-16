package com.phoenix.dating.profile.dto;

import com.phoenix.dating.profile.model.*;
import jakarta.validation.constraints.NotNull;

public record OnboardingPreferencesRequest (
        @NotNull SeekingGender seekingGender,
        @NotNull SeekingRelationshipGoal seekingRelationshipGoal,
        @NotNull SeekingAgeRange seekingAgeRange,
        @NotNull SeekingDistance seekingDistance,
        @NotNull SeekingAttractionFocus seekingAttractionFocus
) {
}
