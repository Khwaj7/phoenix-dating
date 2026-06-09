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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.ok(profileService.getMe());
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
            @PathVariable String userId) {
        return profileService.getById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
