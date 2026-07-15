package com.phoenix.dating.profile.dto;

import com.phoenix.dating.profile.model.*;
import jakarta.validation.constraints.NotNull;

@NotNull
public record OnboardingPreferencesRequest (
        SeekingGender seekingGender,
        SeekingRelationshipGoal seekingRelationshipGoal,
        SeekingAgeRange seekingAgeRange,
        SeekingDistance seekingDistance,
        SeekingAttractionFocus seekingAttractionFocus
) {
}
