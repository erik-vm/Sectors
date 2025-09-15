DROP SCHEMA PUBLIC CASCADE;

-- Drop existing tables if they exist (for clean recreation)
DROP TABLE IF EXISTS submission_sectors CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS user_submissions CASCADE;
DROP TABLE IF EXISTS sectors CASCADE;
DROP TABLE IF EXISTS people CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

DROP SEQUENCE IF EXISTS seq1;

CREATE SEQUENCE seq1 AS INTEGER START WITH 1;
-- =================================================================
-- Roles Table (extends BaseEntity)
-- =================================================================
CREATE TABLE roles (
                       id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('seq1'),
                       role_name VARCHAR(20) NOT NULL UNIQUE,
                       description VARCHAR(255),
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP,
                       CONSTRAINT ck_roles_name CHECK (role_name IN ('USER', 'ADMIN'))
);

-- =================================================================
-- People Table (extends BaseEntity)
-- =================================================================
CREATE TABLE people (
                        id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('seq1'),
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP
);

-- =================================================================
-- Users Table (extends BaseEntity)
-- =================================================================
CREATE TABLE users (
                       id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('seq1'),
                       username VARCHAR(20) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT 1,
                       is_locked BOOLEAN NOT NULL DEFAULT 0,
                       is_expired BOOLEAN NOT NULL DEFAULT 0,
                       credentials_expired BOOLEAN NOT NULL DEFAULT 0,
                       last_login TIMESTAMP,
                       person_id BIGINT,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP,
                       CONSTRAINT ck_users_username_length CHECK (LENGTH(username) >= 5 AND LENGTH(username) <= 20),
                       CONSTRAINT ck_users_email_format CHECK (email LIKE '%@%'),
                       CONSTRAINT fk_users_person FOREIGN KEY (person_id) REFERENCES people(id)
);

-- =================================================================
-- User-Roles Join Table (Many-to-Many)
-- =================================================================
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- =================================================================
-- Sectors Table (Self-referencing for hierarchy, extends BaseEntity)
-- =================================================================
CREATE TABLE sectors (
                         id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('seq1'),
                         name VARCHAR(255) NOT NULL,
                         description VARCHAR(500),
                         level INTEGER NOT NULL DEFAULT 0,
                         sort_order INTEGER,
                         is_active BOOLEAN NOT NULL DEFAULT 1,
                         parent_id BIGINT,
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP,
                         CONSTRAINT fk_sectors_parent FOREIGN KEY (parent_id) REFERENCES sectors(id) ON DELETE CASCADE,
                         CONSTRAINT ck_sectors_level CHECK (level >= 0),
                         CONSTRAINT ck_sectors_name_not_empty CHECK (LENGTH(TRIM(name)) > 0)
);

-- =================================================================
-- User Submissions Table (extends BaseEntity)
-- =================================================================
CREATE TABLE user_submissions (
                                  id BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('seq1'),
                                  agreed_to_terms BOOLEAN NOT NULL,
                                  is_active BOOLEAN NOT NULL DEFAULT 1,
                                  user_id BIGINT NOT NULL,
                                  created_at TIMESTAMP NOT NULL,
                                  updated_at TIMESTAMP,
                                  CONSTRAINT fk_user_submissions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                  CONSTRAINT ck_user_submissions_terms CHECK (agreed_to_terms = 1)
);

-- =================================================================
-- Submission-Sectors Join Table (Many-to-Many)
-- =================================================================
CREATE TABLE submission_sectors (
                                    submission_id BIGINT NOT NULL,
                                    sector_id BIGINT NOT NULL,
                                    PRIMARY KEY (submission_id, sector_id),
                                    CONSTRAINT fk_submission_sectors_submission FOREIGN KEY (submission_id) REFERENCES user_submissions(id) ON DELETE CASCADE,
                                    CONSTRAINT fk_submission_sectors_sector FOREIGN KEY (sector_id) REFERENCES sectors(id) ON DELETE CASCADE
);

-- =================================================================
-- Indexes for Performance
-- =================================================================

-- Users table indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_users_last_login ON users(last_login);
CREATE INDEX idx_users_person ON users(person_id);

-- People table indexes
CREATE INDEX idx_people_first_name ON people(first_name);
CREATE INDEX idx_people_last_name ON people(last_name);
CREATE INDEX idx_people_created_at ON people(created_at);

-- Roles table indexes
CREATE INDEX idx_roles_name ON roles(role_name);
CREATE INDEX idx_roles_created_at ON roles(created_at);

-- Sectors table indexes
CREATE INDEX idx_sectors_name ON sectors(name);
CREATE INDEX idx_sectors_parent ON sectors(parent_id);
CREATE INDEX idx_sectors_level ON sectors(level);
CREATE INDEX idx_sectors_is_active ON sectors(is_active);
CREATE INDEX idx_sectors_sort_order ON sectors(sort_order);
CREATE INDEX idx_sectors_hierarchy ON sectors(parent_id, level, sort_order);
CREATE INDEX idx_sectors_created_at ON sectors(created_at);

-- User submissions table indexes
CREATE INDEX idx_user_submissions_user ON user_submissions(user_id);
CREATE INDEX idx_user_submissions_created_at ON user_submissions(created_at);
CREATE INDEX idx_user_submissions_is_active ON user_submissions(is_active);

-- Join table indexes (additional to primary keys)
CREATE INDEX idx_user_roles_role ON user_roles(role_id);
CREATE INDEX idx_submission_sectors_sector ON submission_sectors(sector_id);

-- =================================================================
-- Comments for Documentation
-- =================================================================

COMMENT ON TABLE roles IS 'System roles (USER, ADMIN) with descriptions, extends BaseEntity';
COMMENT ON TABLE people IS 'Personal information for users, extends BaseEntity';
COMMENT ON TABLE users IS 'User accounts with authentication information, extends BaseEntity';
COMMENT ON TABLE user_roles IS 'Many-to-many relationship between users and roles';
COMMENT ON TABLE sectors IS 'Hierarchical sector structure for business classification, extends BaseEntity';
COMMENT ON TABLE user_submissions IS 'Form submissions by users with sector selections, extends BaseEntity';
COMMENT ON TABLE submission_sectors IS 'Many-to-many relationship between submissions and selected sectors';

COMMENT ON COLUMN roles.role_name IS 'Role name enum (USER, ADMIN)';
COMMENT ON COLUMN users.is_active IS 'Whether the user account is active';
COMMENT ON COLUMN users.is_locked IS 'Whether the user account is locked';
COMMENT ON COLUMN users.is_expired IS 'Whether the user account has expired';
COMMENT ON COLUMN users.credentials_expired IS 'Whether the user credentials have expired';
COMMENT ON COLUMN users.person_id IS 'Reference to the users personal information';

COMMENT ON COLUMN sectors.level IS 'Hierarchy level (0=root, 1=first level child, etc.)';
COMMENT ON COLUMN sectors.sort_order IS 'Custom ordering within the same parent/level';
COMMENT ON COLUMN sectors.is_active IS 'Whether this sector is available for selection';

COMMENT ON COLUMN user_submissions.agreed_to_terms IS 'User agreement to terms and conditions';
COMMENT ON COLUMN user_submissions.is_active IS 'Whether this submission is active';