CREATE TABLE skills (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

                        name VARCHAR(100) NOT NULL,
                        normalized_name VARCHAR(100) NOT NULL,
                        category VARCHAR(30) NOT NULL,

                        created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT uk_skills_normalized_name UNIQUE (normalized_name),

                        CONSTRAINT ck_skills_name_not_blank
                            CHECK (LENGTH(TRIM(name)) >= 2),

                        CONSTRAINT ck_skills_normalized_name_not_blank
                            CHECK (LENGTH(TRIM(normalized_name)) >= 2),

                        CONSTRAINT ck_skills_category
                            CHECK (
                                category IN (
                                             'LANGUAGE',
                                             'FRAMEWORK',
                                             'DATABASE',
                                             'TOOL',
                                             'CLOUD',
                                             'OTHER'
                                    )
                                )
);

CREATE INDEX idx_skills_category
    ON skills (category);

CREATE INDEX idx_skills_name
    ON skills (normalized_name);
