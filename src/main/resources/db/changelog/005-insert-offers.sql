-- Insert do tabeli car_details
INSERT INTO car_details (id, brand, model, year, color, engine_displacement, vin, mileage, fuel_type, transmission, body_type, drive_type, engine_power, doors, seats, condition, registration_number, registration_country, first_owner, accident_free, service_history, additional_features)
VALUES
    (UUID(), 'Toyota', 'Corolla', 2021, 'Czarny', '1.8L', '1HGBH41JXMN109186', 10000, 'PETROL', 'AUTOMATIC', 'SEDAN', 'FWD', 130, 4, 5, 'USED', 'ABC123', 'PL', TRUE, TRUE, TRUE, 'Klimatyzacja, Kamera parkingowa'),
    (UUID(), 'Honda', 'Civic', 2022, 'Srebrny', '1.5L', '2HGBH41JXMN109187', 5000, 'PETROL', 'AUTOMATIC', 'SEDAN', 'FWD', 150, 4, 5, 'USED', 'DEF456', 'PL', TRUE, TRUE, TRUE, 'Klimatyzacja, Kamera parkingowa');

-- Insert do tabeli car_equipment
INSERT INTO car_equipment (id, car_details_id, air_conditioning, automatic_climate, heated_seats, electric_seats, leather_seats, panoramic_roof, electric_windows, electric_mirrors, keyless_entry, wheel_heating, navigation_system, bluetooth, usb_port, multifunction, android_auto, apple_car_play, sound_system)
VALUES
    (UUID(), 'UUID_z_car_details_1', TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE),
    (UUID(), 'UUID_z_car_details_2', TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE);

-- Insert do tabeli offer
INSERT INTO offers (id, title, description, car_details_id, price, currency, location, contact_phone, contact_email, created_at, updated_at, expiration_date, view_count, featured, negotiable)
VALUES
    (UUID(), 'Sprzedam Toyotę', 'Bardzo dobry stan, mało używana.', 'UUID_z_car_details_1', 45000, 'PLN', 'Warszawa', '123456789', 'kontakt@toyota.pl', NOW(), NOW(), '2025-12-31', 0, FALSE, TRUE),
    (UUID(), 'Sprzedam Hondę', 'Bardzo dobry stan, prawie nowa.', 'UUID_z_car_details_2', 40000, 'PLN', 'Wrocław', '987654321', 'kontakt@honda.pl', NOW(), NOW(), '2025-12-31', 0, FALSE, FALSE);

-- Insert do tabeli offer_images
INSERT INTO offer_images (id, url, caption, primary, sort_order)
VALUES
    (UUID(), 'classpath:/images/toyota.jpg', 'Zdjęcie Toyoty', TRUE, 1),
    (UUID(), 'classpath:/images/honda.jpg', 'Zdjęcie Hondy', TRUE, 1);
