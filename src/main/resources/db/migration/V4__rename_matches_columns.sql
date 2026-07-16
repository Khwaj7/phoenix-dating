-- Aligns matches columns with the Match entity (matcher/matched).
-- Conditional: the dev database was already renamed manually, fresh databases were not.
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema = current_schema() AND table_name = 'matches' AND column_name = 'user1_id') THEN
        ALTER TABLE matches RENAME COLUMN user1_id TO matcher;
    END IF;

    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema = current_schema() AND table_name = 'matches' AND column_name = 'user2_id') THEN
        ALTER TABLE matches RENAME COLUMN user2_id TO matched;
    END IF;
END $$;
