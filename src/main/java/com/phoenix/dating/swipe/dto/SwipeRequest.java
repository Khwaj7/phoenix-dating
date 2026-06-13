package com.phoenix.dating.swipe.dto;

import com.phoenix.dating.swipe.model.SwipeAction;

public record SwipeRequest(
        SwipeAction action
) {
}
