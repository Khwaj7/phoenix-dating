package com.phoenix.dating.swipe.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record SwipeResponse(
        boolean matched,
        @Nullable UUID matchId
        ) {
}
