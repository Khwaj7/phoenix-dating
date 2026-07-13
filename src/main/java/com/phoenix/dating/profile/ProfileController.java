package com.phoenix.dating.profile;

import com.phoenix.dating.profile.dto.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ProfileResponse> getMyProfile() {
        UUID currentUserId = UUID.fromString("2cc245ed-78c6-4aa7-95f1-87e05387f0e7");

        return ResponseEntity.ok(profileService.getMe(currentUserId));
    }

    @Operation(
            summary = "Get profiles to swipe",
            description = "Returns a batch of candidate profiles for the swipe deck, excluding the current user. "
                    + "Profiles are returned in random order."
    )
    @ApiResponse(responseCode = "200", description = "Batch of candidate profiles")
    @GetMapping("/discover")
    public ResponseEntity<List<ProfileResponse>> discoverProfiles(
            @Parameter(description = "Maximum number of profiles to return", example = "10")
            @RequestParam(defaultValue = "10") int limit) {
        UUID currentUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        return ResponseEntity.ok(profileService.getDiscoveryFeed(currentUserId, limit));
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
    public ProfileResponse getProfileById(
            @Parameter(description = "User identifier (UUID)", example = "550e8400-e29b-41d4-a716-446655440001")
            @PathVariable UUID userId) {
        return profileService.getById(userId);
    }
}
