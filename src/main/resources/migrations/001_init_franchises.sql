CREATE TABLE IF NOT EXISTS franchise (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS branch (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    franchise_id UUID NOT NULL REFERENCES franchise(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    UNIQUE (franchise_id, name)
);

CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    branch_id UUID NOT NULL REFERENCES branch(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL,
    UNIQUE (branch_id, name)
);

