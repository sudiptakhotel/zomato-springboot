--Insert app_user

--INSERT INTO app_user (name, email, password) VALUES
--('Aarav Patel', 'aarav.patel@example.com', 'password123'),
--('Vihaan Sharma', 'vihaan.sharma@example.com', 'password123'),
--('Anaya Mehta', 'anaya.mehta@example.com', 'password123'),
--('Aditya Singh', 'aditya.singh@example.com', 'password123'),
--('Aanya Gupta', 'aanya.gupta@example.com', 'password123'),
--('Kabir Rao', 'kabir.rao@example.com', 'password123'),
--('Ishaan Iyer', 'ishaan.iyer@example.com', 'password123'),
--('Saanvi Nair', 'saanvi.nair@example.com', 'password123'),
--('Arjun Reddy', 'arjun.reddy@example.com', 'password123'),
--('Diya Shah', 'diya.shah@example.com', 'password123'),
--('Krishna Kumar', 'krishna.kumar@example.com', 'password123'),
--('Rohan Verma', 'rohan.verma@example.com', 'password123'),
--('Aarohi Desai', 'aarohi.desai@example.com', 'password123'),
--('Dhruv Joshi', 'dhruv.joshi@example.com', 'password123'),
--('Sara Kapoor', 'sara.kapoor@example.com', 'password123'),
--('Vivaan Jain', 'vivaan.jain@example.com', 'password123'),
--('Aadhya Malik', 'aadhya.malik@example.com', 'password123'),
--('Mihir Thakur', 'mihir.thakur@example.com', 'password123'),
--('Tara Roy', 'tara.roy@example.com', 'password123'),
--('Kian Sen', 'kian.sen@example.com', 'password123');

INSERT INTO app_user (name, email, password, phone_number) VALUES ('Amit Sharma', 'amit.sharma@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543210');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Sneha Patel', 'sneha.patel@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543211');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Rajesh Kumar', 'rajesh.kumar@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543212');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Priya Reddy', 'priya.reddy@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543213');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Anil Singh', 'anil.singh@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543214');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Ravi Verma', 'ravi.verma@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543215');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Neha Mehta', 'neha.mehta@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543216');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Vikram Rao', 'vikram.rao@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543217');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Sanjay Gupta', 'sanjay.gupta@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543218');
INSERT INTO app_user (name, email, password, phone_number) VALUES ('Divya Nair', 'divya.nair@example.com', '$2a$10$DUMMYENCRYPTEDPASSWORD', '9876543219');


-- Assuming the users have been inserted and IDs are sequential starting from 1.

INSERT INTO user_entity_role (user_entity_id, role) VALUES
(1, 'CUSTOMER'),
(2, 'CUSTOMER'),
(3, 'CUSTOMER'),
(4, 'PARTNER'),
(5, 'PARTNER'),
(6, 'CUSTOMER'),
(7, 'CUSTOMER'),
(8, 'CUSTOMER'),
(9, 'PARTNER'),
(10, 'PARTNER');

--Insert Customer
INSERT INTO customer (user_id) VALUES (1);

--Insert Partner
INSERT INTO partner (user_id , available , current_location) VALUES (2 , true , ST_GeomFromText('POINT(88.3644 22.5731)', 4326));

-- Insert Restaurants
INSERT INTO restaurant (id, name, email, is_open, opening_time, closing_time, is_veg, is_recommended_for_child, restaurant_location) VALUES
(1, 'Flavors of Bengal', 'contact@flavorsofbengal.com', true, '09:00:00', '23:00:00', true, true, ST_GeomFromText('POINT(88.3639 22.5726)', 4326)),
(2, 'The Spice Route', 'info@spiceroutekolkata.com', true, '10:00:00', '22:00:00', false, false, ST_GeomFromText('POINT(88.3640 22.5727)', 4326)),
(3, 'Curry Corner', 'contact@currycornerkolkata.com', true, '11:00:00', '23:00:00', true, true, ST_GeomFromText('POINT(88.3641 22.5728)', 4326)),
(4, 'Taste of India', 'info@tasteofindiakolkata.com', true, '09:00:00', '21:00:00', false, false, ST_GeomFromText('POINT(88.3642 22.5729)', 4326)),
(5, 'Biryani House', 'contact@biryanihousekolkata.com', true, '08:00:00', '22:00:00', false, true, ST_GeomFromText('POINT(88.3643 22.5730)', 4326)),
(6, 'The Veggie Delight', 'info@veggiedelightkolkata.com', true, '09:00:00', '03:00:00', true, true, ST_GeomFromText('POINT(88.3644 22.5731)', 4326)),
(7, 'Masala Magic', 'contact@masalamagickolkata.com', true, '10:00:00', '23:00:00', false, false, ST_GeomFromText('POINT(88.3645 22.5732)', 4326)),
(8, 'Mughlai Treat', 'info@mughlaitreatkolkata.com', true, '11:00:00', '21:00:00', false, false, ST_GeomFromText('POINT(88.3646 22.5733)', 4326)),
(9, 'South Spice', 'contact@southspicekolkata.com', true, '09:00:00', '22:00:00', true, true, ST_GeomFromText('POINT(88.3647 22.5734)', 4326)),
(10, 'Kolkata Bites', 'info@kolkatabites.com', true, '10:00:00', '23:00:00', false, true, ST_GeomFromText('POINT(88.3648 22.5735)', 4326));

-- Insert Menu Items
INSERT INTO menu_item (id, name, description, price, available, restaurant_id) VALUES
(1, 'Paneer Butter Masala', 'Creamy tomato-based curry with paneer.', 250.00, true, 1),
(2, 'Chicken Biryani', 'Spiced basmati rice with marinated chicken.', 300.00, true, 2),
(3, 'Aloo Gobi', 'Stir-fried potatoes and cauliflower.', 180.00, true, 3),
(4, 'Mutton Rogan Josh', 'Kashmiri-style slow-cooked lamb curry.', 350.00, true, 4),
(5, 'Vegetable Pulao', 'Aromatic basmati rice with mixed vegetables.', 200.00, true, 5),
(6, 'Chole Bhature', 'Spicy chickpeas with deep-fried bread.', 220.00, true, 6),
(7, 'Dal Makhani', 'Slow-cooked black lentils in a creamy sauce.', 210.00, true, 7),
(8, 'Hyderabadi Biryani', 'Flavorful basmati rice with mutton.', 320.00, true, 8),
(9, 'Masala Dosa', 'Crispy dosa filled with spiced potatoes.', 150.00, true, 9),
(10, 'Fish Curry', 'Bengali-style fish curry with mustard seeds.', 280.00, true, 10),
(11, 'Palak Paneer', 'Spinach puree cooked with paneer cubes.', 240.00, true, 1),
(12, 'Butter Chicken', 'Chicken cooked in a buttery tomato gravy.', 290.00, true, 2),
(13, 'Kadai Paneer', 'Paneer cooked with bell peppers and onions.', 230.00, true, 3),
(14, 'Prawn Malai Curry', 'Prawns cooked in a coconut milk gravy.', 350.00, true, 4),
(15, 'Veg Biryani', 'Fragrant rice cooked with mixed vegetables.', 200.00, true, 5),
(16, 'Rajma Chawal', 'Red kidney beans cooked in a thick gravy.', 180.00, true, 6),
(17, 'Paneer Tikka', 'Grilled paneer with spices.', 250.00, true, 7),
(18, 'Lamb Biryani', 'Spiced rice cooked with tender lamb pieces.', 350.00, true, 8),
(19, 'Idli Sambar', 'Steamed rice cakes served with lentil soup.', 120.00, true, 9),
(20, 'Kolkata Kathi Roll', 'Flatbread filled with egg, chicken, and spices.', 200.00, true, 10);

--Insert UserWallet

INSERT INTO user_wallet (balance, user_id)
VALUES
(500.0, 1),
(600.0 , 2);

--Insert RestaurantWallet

INSERT INTO restaurant_wallet (balance, restaurant_id)
VALUES (1000.0, 6);

--INSERT PROMO
INSERT INTO promo (promo_name, expiration_date, discount_percentage, threshold_amount, description)
VALUES
('SAVE50', '2024-12-31', 50, 1000.00, 'Save 50% on orders above ₹1000'),
('FLAT100', '2024-11-30', 100, 500.00, 'Flat ₹100 off on orders above ₹500'),
('NEWUSER20', '2024-12-31', 20, 0.00, '20% off for new users, no minimum order'),
('FESTIVE30', '2024-12-25', 30, 1500.00, '30% off on festive season orders above ₹1500'),
('SUPER10', '2024-10-15', 10, 100.00, '10% off on orders above ₹100'),
('WEEKEND20', '2024-10-31', 20, 200.00, '20% off on weekend orders above ₹200'),
('MONTHEND', '2024-09-30', 25, 500.00, '25% off on month-end orders above ₹500'),
('PARTY500', '2024-12-31', 500, 2500.00, '₹500 off on party orders above ₹2500'),
('DIWALI50', '2024-11-12', 50, 2000.00, '50% off on Diwali orders above ₹2000'),
('WELCOME100', '2024-12-31', 100, 0.00, '₹100 off on your first order'),
('LOVERIDES', '2024-10-10', 15, 500.00, '15% off on rides above ₹500'),
('SUPERDEAL', '2024-12-31', 50, 1000.00, 'Super deal: 50% off on orders above ₹1000'),
('LUNCHTIME', '2024-11-15', 30, 300.00, '30% off during lunchtime on orders above ₹300'),
('DINNER50', '2024-12-20', 50, 500.00, '50% off on dinner orders above ₹500'),
('EARLYBIRD', '2024-09-30', 15, 200.00, '15% off on early morning orders above ₹200'),
('NIGHTOWL', '2024-11-30', 25, 300.00, '25% off on late-night orders above ₹300'),
('FOODIE100', '2024-12-31', 100, 1000.00, '₹100 off on food orders above ₹1000'),
('FAMILY50', '2024-12-31', 50, 2000.00, '50% off on family orders above ₹2000'),
('WINTER20', '2024-12-31', 20, 500.00, '20% off on winter season orders above ₹500'),
('BIGSALE', '2024-12-31', 75, 2500.00, '75% off on orders above ₹2500');


