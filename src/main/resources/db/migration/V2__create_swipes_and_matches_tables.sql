CREATE TABLE IF NOT EXISTS swipes (
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    swiper_id  UUID        NOT NULL,
    swiped_id  UUID        NOT NULL,
    action     VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_swipes PRIMARY KEY (id),
    CONSTRAINT uq_swipe_swiper_swiped UNIQUE (swiper_id, swiped_id),
    CONSTRAINT chk_swipes_action CHECK (action IN ('ACCEPT', 'REFUSE')),
    CONSTRAINT fk_swipes_swiper FOREIGN KEY (swiper_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_swipes_swiped FOREIGN KEY (swiped_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_swipes_swiper_id ON swipes (swiper_id);
CREATE INDEX idx_swipes_swiped_id ON swipes (swiped_id);
CREATE INDEX idx_swipes_swiper_swiped_action ON swipes (swiper_id, swiped_id, action);

CREATE TABLE IF NOT EXISTS matches (
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    user1_id   UUID        NOT NULL,
    user2_id   UUID        NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_matches PRIMARY KEY (id),
    CONSTRAINT uq_match_users UNIQUE (user1_id, user2_id),
    CONSTRAINT fk_matches_user1 FOREIGN KEY (user1_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_matches_user2 FOREIGN KEY (user2_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_matches_user1_id ON matches (user1_id);
CREATE INDEX idx_matches_user2_id ON matches (user2_id);
