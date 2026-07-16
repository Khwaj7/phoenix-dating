CREATE TABLE IF NOT EXISTS user_preferences (
    user_id                    UUID        NOT NULL,
    seeking_gender             VARCHAR(20) NOT NULL,
    seeking_relationship_goal  VARCHAR(20) NOT NULL,
    seeking_age_range          VARCHAR(20) NOT NULL,
    seeking_distance           VARCHAR(20) NOT NULL,
    seeking_attraction_focus   VARCHAR(20) NOT NULL,
    created_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_user_preferences PRIMARY KEY (user_id),
    CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_user_preferences_seeking_gender CHECK (seeking_gender IN ('MALE', 'FEMALE', 'NON_BINARY', 'OTHER', 'ANY')),
    CONSTRAINT chk_user_preferences_seeking_relationship_goal CHECK (seeking_relationship_goal IN ('SHORT', 'LONG', 'UNSURE', 'ANY')),
    CONSTRAINT chk_user_preferences_seeking_age_range CHECK (seeking_age_range IN ('CLOSE', 'YOUNGER', 'OLDER', 'ANY')),
    CONSTRAINT chk_user_preferences_seeking_distance CHECK (seeking_distance IN ('NEARBY', 'SAME_CITY', 'OPEN_TO_TRAVEL', 'ANY')),
    CONSTRAINT chk_user_preferences_seeking_attraction_focus CHECK (seeking_attraction_focus IN ('PERSONALITY', 'APPEARANCE', 'HUMOR', 'OTHER'))
);
