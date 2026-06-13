package com.phoenix.dating.swipe;

import com.phoenix.dating.swipe.dto.SwipeRequest;
import com.phoenix.dating.swipe.dto.SwipeResponse;
import org.springframework.stereotype.Service;

@Service
public class SwipeService {

    public SwipeResponse postSwipeAction(String userId, SwipeRequest swipeRequest) {
        return new SwipeResponse(true, null);
    }
}
