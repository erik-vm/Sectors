-- =================================================================
-- Initial Data for Sectors Management System (PostgreSQL)
-- =================================================================

-- =================================================================
-- Insert Roles (using role_name column and current timestamp)
-- =================================================================
INSERT INTO roles (role_name, description, created_at, updated_at)
VALUES ('USER', 'Standard user with basic access permissions', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ADMIN', 'Administrator with elevated permissions', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =================================================================
-- Insert Admin Person and User
-- =================================================================

-- Insert Person for Admin (ID will be 3, after roles 1,2)
INSERT INTO people (first_name, last_name, created_at, updated_at)
VALUES ('Erik', 'Vainumäe', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Admin User (ID will be 4, with person_id=3)
-- Password 'admin' encoded with BCrypt: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jW9TukjKkDOW
INSERT INTO users (username, email, password, is_active, is_locked, is_expired, credentials_expired, person_id, created_at, updated_at)
VALUES ('admin', 'admin@erik.vm', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9P8jW9TukjKkDOW', TRUE, FALSE, FALSE, FALSE, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Assign ADMIN role to admin user (user_id=4, role_id=2 for ADMIN)
INSERT INTO user_roles (user_id, role_id)
VALUES (4, 2);

-- =================================================================
-- Insert Sector Hierarchy (Complete hierarchy from DataLoader)
-- =================================================================

-- Level 0: Root sectors (IDs: Manufacturing=5, Service=6, Other=7)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Manufacturing', 'Manufacturing sector', 0, 1, TRUE, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Service', 'Service sector', 0, 2, TRUE, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other', 'Other sectors', 0, 3, TRUE, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Manufacturing subcategories (Manufacturing has id=5)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Construction materials', 'Construction materials', 1, 1, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Electronics and Optics', 'Electronics and Optics', 1, 2, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Food and Beverage', 'Food and Beverage', TRUE, 3, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Furniture', 'Furniture', 1, 4, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Machinery', 'Machinery', TRUE, 5, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Metalworking', 'Metalworking', 1, 6, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Plastic and Rubber', 'Plastic and Rubber', 1, 7, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Printing', 'Printing', TRUE, 8, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Textile and Clothing', 'Textile and Clothing', TRUE, 9, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Wood', 'Wood', TRUE, 10, TRUE, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Service subcategories (Service has id=6)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Business services', 'Business services', 1, 1, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Engineering', 'Engineering', 1, 2, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Information Technology and Telecommunications', 'Information Technology and Telecommunications', TRUE, 3, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Tourism', 'Tourism', TRUE, 4, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Translation services', 'Translation services', TRUE, 5, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Transport and Logistics', 'Transport and Logistics', 1, 6, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Other subcategories (Other has id=7)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Creative industries', 'Creative industries', 1, 1, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Energy technology', 'Energy technology', 1, 2, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Environment', 'Environment', 1, 3, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Food and Beverage subcategories (Food and Beverage has id=10)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Bakery & confectionery products', 'Bakery & confectionery products', 2, 1, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Beverages', 'Beverages', 2, 2, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Fish & fish products', 'Fish & fish products', 2, 3, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Meat & meat products', 'Meat & meat products', 2, 4, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Milk & dairy products', 'Milk & dairy products', 2, 5, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other', 'Other food products', 2, 6, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Sweets & snack food', 'Sweets & snack food', 2, 7, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Furniture subcategories (Furniture has id=11)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Bathroom/sauna', 'Bathroom/sauna furniture', 2, 1, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Bedroom', 'Bedroom furniture', 2, 2, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Children''s room', 'Children''s room furniture', 2, 3, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Kitchen', 'Kitchen furniture', 2, 4, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Living room', 'Living room furniture', 2, 5, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Office', 'Office furniture', 2, 6, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other (Furniture)', 'Other furniture', 2, 7, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Outdoor', 'Outdoor furniture', 2, 8, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Project furniture', 'Project furniture', 2, 9, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Machinery subcategories (Machinery has id=12)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Machinery components', 'Machinery components', 2, 1, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Machinery equipment/tools', 'Machinery equipment/tools', 2, 2, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Manufacture of machinery', 'Manufacture of machinery', 2, 3, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Maritime', 'Maritime', 2, 4, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Metal structures', 'Metal structures', 2, 5, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other', 'Other machinery', 2, 6, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Repair and maintenance service', 'Repair and maintenance service', 2, 7, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =================================================================
-- End of Data File
-- NOTE: Additional sectors from DataLoader could be added here
-- Users will be created through registration form
-- =================================================================