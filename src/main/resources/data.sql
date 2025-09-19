-- =================================================================
-- Initial Data for Sectors Management System (PostgreSQL)
-- =================================================================

-- =================================================================
-- Note: Roles and Admin User are now created by DataInitializer
-- =================================================================

-- =================================================================
-- Complete Sector Hierarchy (Based on index.html reference)
-- =================================================================

-- Level 0: Root sectors (Manufacturing=1, Service=2, Other=3)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Manufacturing', 'Manufacturing sector', 0, 1, TRUE, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Service', 'Service sector', 0, 2, TRUE, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other', 'Other sectors', 0, 3, TRUE, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Manufacturing subcategories (Manufacturing id=1)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Construction materials', 'Construction materials', 1, 1, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Electronics and Optics', 'Electronics and Optics', 1, 2, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Food and Beverage', 'Food and Beverage', 1, 3, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Furniture', 'Furniture', 1, 4, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Machinery', 'Machinery', 1, 5, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Metalworking', 'Metalworking', 1, 6, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Plastic and Rubber', 'Plastic and Rubber', 1, 7, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Printing', 'Printing', 1, 8, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Textile and Clothing', 'Textile and Clothing', 1, 9, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Wood', 'Wood', 1, 10, TRUE, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Food and Beverage subcategories (Food and Beverage id=6)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Bakery & confectionery products', 'Bakery & confectionery products', 2, 1, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Beverages', 'Beverages', 2, 2, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Fish & fish products', 'Fish & fish products', 2, 3, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Meat & meat products', 'Meat & meat products', 2, 4, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Milk & dairy products', 'Milk & dairy products', 2, 5, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Other', 'Other food products', 2, 6, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Sweets & snack food', 'Sweets & snack food', 2, 7, TRUE, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Furniture subcategories (Furniture id=7)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Bathroom/sauna', 'Bathroom/sauna furniture', 2, 1, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Bedroom', 'Bedroom furniture', 2, 2, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Children''s room', 'Children''s room furniture', 2, 3, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Kitchen', 'Kitchen furniture', 2, 4, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Living room', 'Living room furniture', 2, 5, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Office', 'Office furniture', 2, 6, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Other (Furniture)', 'Other furniture', 2, 7, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Outdoor', 'Outdoor furniture', 2, 8, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Project furniture', 'Project furniture', 2, 9, TRUE, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Machinery subcategories (Machinery id=8)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Machinery components', 'Machinery components', 2, 1, TRUE, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Machinery equipment/tools', 'Machinery equipment/tools', 2, 2, TRUE, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Manufacture of machinery', 'Manufacture of machinery', 2, 3, TRUE, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Maritime', 'Maritime', 2, 4, TRUE, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Metal structures', 'Metal structures', 2, 5, TRUE, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Other', 'Other machinery', 2, 6, TRUE, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Repair and maintenance service', 'Repair and maintenance service', 2, 7, TRUE, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 3: Maritime subcategories (Maritime id=26)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Aluminium and steel workboats', 'Aluminium and steel workboats', 3, 1, TRUE, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Boat/Yacht building', 'Boat/Yacht building', 3, 2, TRUE, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Ship repair and conversion', 'Ship repair and conversion', 3, 3, TRUE, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Metalworking subcategories (Metalworking id=9)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Construction of metal structures', 'Construction of metal structures', 2, 1, TRUE, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Houses and buildings', 'Houses and buildings', 2, 2, TRUE, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Metal products', 'Metal products', 2, 3, TRUE, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Metal works', 'Metal works', 2, 4, TRUE, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 3: Metal works subcategories (Metal works id=32)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('CNC-machining', 'CNC-machining', 3, 1, TRUE, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Forgings, Fasteners', 'Forgings, Fasteners', 3, 2, TRUE, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Gas, Plasma, Laser cutting', 'Gas, Plasma, Laser cutting', 3, 3, TRUE, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('MIG, TIG, Aluminum welding', 'MIG, TIG, Aluminum welding', 3, 4, TRUE, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Plastic and Rubber subcategories (Plastic and Rubber id=10)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Packaging', 'Packaging', 2, 1, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Plastic goods', 'Plastic goods', 2, 2, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Plastic processing technology', 'Plastic processing technology', 2, 3, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Plastic profiles', 'Plastic profiles', 2, 4, TRUE, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 3: Plastic processing technology subcategories (Plastic processing technology id=39)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Blowing', 'Blowing', 3, 1, TRUE, 39, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Moulding', 'Moulding', 3, 2, TRUE, 39, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Plastics welding and processing', 'Plastics welding and processing', 3, 3, TRUE, 39, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Printing subcategories (Printing id=11)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Advertising', 'Advertising', 2, 1, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Book/Periodicals printing', 'Book/Periodicals printing', 2, 2, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Labelling and packaging printing', 'Labelling and packaging printing', 2, 3, TRUE, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Textile and Clothing subcategories (Textile and Clothing id=12)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Clothing', 'Clothing', 2, 1, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Textile', 'Textile', 2, 2, TRUE, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Wood subcategories (Wood id=13)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Other (Wood)', 'Other (Wood)', 2, 1, TRUE, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Wooden building materials', 'Wooden building materials', 2, 2, TRUE, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Wooden houses', 'Wooden houses', 2, 3, TRUE, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Service subcategories (Service id=2)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Business services', 'Business services', 1, 1, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Engineering', 'Engineering', 1, 2, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Information Technology and Telecommunications', 'Information Technology and Telecommunications', 1, 3, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Tourism', 'Tourism', 1, 4, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Translation services', 'Translation services', 1, 5, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Transport and Logistics', 'Transport and Logistics', 1, 6, TRUE, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Information Technology subcategories (IT id=51)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Data processing, Web portals, E-marketing', 'Data processing, Web portals, E-marketing', 2, 1, TRUE, 51, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Programming, Consultancy', 'Programming, Consultancy', 2, 2, TRUE, 51, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Software, Hardware', 'Software, Hardware', 2, 3, TRUE, 51, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Telecommunications', 'Telecommunications', 2, 4, TRUE, 51, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 2: Transport and Logistics subcategories (Transport id=53)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Air', 'Air', 2, 1, TRUE, 53, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Rail', 'Rail', 2, 2, TRUE, 53, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Road', 'Road', 2, 3, TRUE, 53, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Water', 'Water', 2, 4, TRUE, 53, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Level 1: Other subcategories (Other id=3)
INSERT INTO sectors (name, description, level, sort_order, is_active, parent_id, created_at, updated_at)
VALUES ('Creative industries', 'Creative industries', 1, 1, TRUE, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Energy technology', 'Energy technology', 1, 2, TRUE, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Environment', 'Environment', 1, 3, TRUE, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- =================================================================
-- End of Data File
-- NOTE: Additional sectors from DataLoader could be added here
-- Users will be created through registration form
-- =================================================================