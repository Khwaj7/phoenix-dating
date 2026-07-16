package com.phoenix.dating.profile;

import com.phoenix.dating.profile.dto.OnboardingPreferencesRequest;
import com.phoenix.dating.profile.dto.ProfileResponse;
import com.phoenix.dating.repository.user.UserEntity;
import com.phoenix.dating.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Tag(name = "Profiles", description = "User profile lookup")
public class ProfileController {

    private final ProfileService profileService;

    @Operation(
            summary = "Get my profile",
            description = "Returns the profile of the currently authenticated user."
    )
    @ApiResponse(responseCode = "200", description = "Profile of the current user")
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(@CurrentUser UserEntity currentUser) {
        return ResponseEntity.ok(profileService.getMe(currentUser.getId()));
    }

    @Operation(
            summary = "Get profiles to swipe",
            description = "Returns a batch of candidate profiles for the swipe deck, excluding the current user. "
                    + "Profiles are returned in random order."
    )
    @ApiResponse(responseCode = "200", description = "Batch of candidate profiles")
    @GetMapping("/discover")
    public ResponseEntity<List<ProfileResponse>> discoverProfiles(
            @CurrentUser UserEntity currentUser,
            @Parameter(description = "Maximum number of profiles to return", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(profileService.getDiscoveryFeed(currentUser.getId(), limit));
    }

    @Operation(
            summary = "Get a profile by user id",
            description = "Returns the public profile of the user matching the given identifier."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "No profile for this identifier", content = @Content)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfileById(
            @Parameter(description = "User identifier (UUID)", example = "550e8400-e29b-41d4-a716-446655440001")
            @PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getById(userId));
    }

    @Operation(
            summary = "Onboard a new user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Preferences saved"),
            @ApiResponse(responseCode = "400", description = "Invalid onboarding preferences")
    })
    @PostMapping("/onboard")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void onboardProfile(
            @CurrentUser UserEntity currentUser,
            @Valid @RequestBody OnboardingPreferencesRequest request) {
        profileService.onboardUser(currentUser, request);
    }
}
