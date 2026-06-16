CREATE TABLE IF NOT EXISTS users (
    id            UUID         NOT NULL DEFAULT gen_random_uuid(),
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    display_name  VARCHAR(100) NOT NULL,
    bio           TEXT,
    birth_date    DATE         NOT NULL,
    gender        VARCHAR(20)  NOT NULL,
    city          VARCHAR(100),
    country       VARCHAR(100),
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_gender CHECK (gender IN ('MALE', 'FEMALE', 'NON_BINARY', 'OTHER'))
);

CREATE INDEX idx_users_email ON users (email);

CREATE TABLE IF NOT EXISTS user_photos (
    user_id   UUID          NOT NULL,
    photo_url VARCHAR(2048) NOT NULL,
    position  INT           NOT NULL,

    CONSTRAINT pk_user_photos PRIMARY KEY (user_id, position),
    CONSTRAINT fk_user_photos_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_user_photos_user_id ON user_photos (user_id);
