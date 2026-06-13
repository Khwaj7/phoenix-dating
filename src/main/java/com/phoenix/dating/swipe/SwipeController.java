package com.phoenix.dating.swipe;

import com.phoenix.dating.swipe.dto.SwipeRequest;
import com.phoenix.dating.swipe.dto.SwipeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swipes")
@RequiredArgsConstructor
@Tag(name = "Swipes", description = "Swipe actions")
public class SwipeController {
    private final SwipeService swipeService;

    @Operation(
            summary = "Swipe a profile",
            description = "Returns the status of the swipe"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Swipe sent"),
            @ApiResponse(responseCode = "500", description = "Swipe action failed")
    })
    @PostMapping("/{userId}")
    public ResponseEntity<SwipeResponse> postSwipeAction(
            @Parameter(description = "User identifier (UUID)", example = "550e8400-e29b-41d4-a716-446655440001")
            @PathVariable String userId,
            @Parameter(description = "Swipe action made by the user", example = "ACCEPT")
            @RequestBody SwipeRequest swipeAction
    ) {
        return ResponseEntity.status(201).body(swipeService.postSwipeAction(userId, swipeAction));
    }
}
