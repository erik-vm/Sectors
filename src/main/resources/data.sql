-- =================================================================
-- Initial Data for Sectors Management System (PostgreSQL/HSQLDB)
-- =================================================================

-- =================================================================
-- Insert Roles (using role_name column and current timestamp)
-- =================================================================
INSERT INTO roles (role_name, description, created_at, updated_at)
VALUES ('USER', 'Standard user with basic access permissions', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ADMIN', 'Administrator with elevated permissions', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =================================================================
-- Insert Sector Hierarchy (Complete hierarchy from DataLoader)
-- =================================================================

-- Level 0: Root sectors
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Manufacturing', 'Manufacturing sector', 0, 1, 1, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Service', 'Service sector', 0, 2, 1, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other', 'Other sectors', 0, 3, 1, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Manufacturing subcategories (Manufacturing has id=3, after roles 1,2)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Construction materials', 'Construction materials', 1, 1, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Electronics and Optics', 'Electronics and Optics', 1, 2, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Food and Beverage', 'Food and Beverage', 1, 3, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Furniture', 'Furniture', 1, 4, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Machinery', 'Machinery', 1, 5, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Metalworking', 'Metalworking', 1, 6, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Plastic and Rubber', 'Plastic and Rubber', 1, 7, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Printing', 'Printing', 1, 8, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Textile and Clothing', 'Textile and Clothing', 1, 9, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Wood', 'Wood', 1, 10, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Service subcategories (Service has id=4)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Business services', 'Business services', 1, 1, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Engineering', 'Engineering', 1, 2, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Information Technology and Telecommunications', 'Information Technology and Telecommunications', 1, 3, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Tourism', 'Tourism', 1, 4, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Translation services', 'Translation services', 1, 5, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Transport and Logistics', 'Transport and Logistics', 1, 6, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Other subcategories (Other has id=5)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Creative industries', 'Creative industries', 1, 1, 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Energy technology', 'Energy technology', 1, 2, 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Environment', 'Environment', 1, 3, 1, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Food and Beverage subcategories (Food and Beverage has id=8)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Bakery & confectionery products', 'Bakery & confectionery products', 2, 1, 1, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Beverages', 'Beverages', 2, 2, 1, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Fish & fish products', 'Fish & fish products', 2, 3, 1, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Meat & meat products', 'Meat & meat products', 2, 4, 1, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Milk & dairy products', 'Milk & dairy products', 2, 5, 1, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other', 'Other food products', 2, 6, 1, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Sweets & snack food', 'Sweets & snack food', 2, 7, 1, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Furniture subcategories (Furniture has id=9)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Bathroom/sauna', 'Bathroom/sauna furniture', 2, 1, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Bedroom', 'Bedroom furniture', 2, 2, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Children''s room', 'Children''s room furniture', 2, 3, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Kitchen', 'Kitchen furniture', 2, 4, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Living room', 'Living room furniture', 2, 5, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Office', 'Office furniture', 2, 6, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other (Furniture)', 'Other furniture', 2, 7, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Outdoor', 'Outdoor furniture', 2, 8, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Project furniture', 'Project furniture', 2, 9, 1, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Machinery subcategories (Machinery has id=10)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Machinery components', 'Machinery components', 2, 1, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Machinery equipment/tools', 'Machinery equipment/tools', 2, 2, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Manufacture of machinery', 'Manufacture of machinery', 2, 3, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Maritime', 'Maritime', 2, 4, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Metal structures', 'Metal structures', 2, 5, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other', 'Other machinery', 2, 6, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Repair and maintenance service', 'Repair and maintenance service', 2, 7, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =================================================================
-- End of Data File
-- NOTE: Additional sectors from DataLoader could be added here
-- Users will be created through registration form
-- =================================================================