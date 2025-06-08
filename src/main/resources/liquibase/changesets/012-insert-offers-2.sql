-- changeset s24399:012-insert-offers-2
-- comment Generate additional offers batch 2

-- Declaration of variables for storing generated UUIDs
DO $$
DECLARE
car_equipment_id8 UUID := gen_random_uuid();
    car_equipment_id9 UUID := gen_random_uuid();
    car_equipment_id10 UUID := gen_random_uuid();
    car_equipment_id11 UUID := gen_random_uuid();
    car_equipment_id12 UUID := gen_random_uuid();
    car_equipment_id13 UUID := gen_random_uuid();
    car_equipment_id14 UUID := gen_random_uuid();
    car_equipment_id15 UUID := gen_random_uuid();
    car_equipment_id16 UUID := gen_random_uuid();
    car_equipment_id17 UUID := gen_random_uuid();
    car_equipment_id18 UUID := gen_random_uuid();
    car_equipment_id19 UUID := gen_random_uuid();
    car_equipment_id20 UUID := gen_random_uuid();

    car_details_id8 UUID := gen_random_uuid();
    car_details_id9 UUID := gen_random_uuid();
    car_details_id10 UUID := gen_random_uuid();
    car_details_id11 UUID := gen_random_uuid();
    car_details_id12 UUID := gen_random_uuid();
    car_details_id13 UUID := gen_random_uuid();
    car_details_id14 UUID := gen_random_uuid();
    car_details_id15 UUID := gen_random_uuid();
    car_details_id16 UUID := gen_random_uuid();
    car_details_id17 UUID := gen_random_uuid();
    car_details_id18 UUID := gen_random_uuid();
    car_details_id19 UUID := gen_random_uuid();
    car_details_id20 UUID := gen_random_uuid();

    offer_id8 UUID := gen_random_uuid();
    offer_id9 UUID := gen_random_uuid();
    offer_id10 UUID := gen_random_uuid();
    offer_id11 UUID := gen_random_uuid();
    offer_id12 UUID := gen_random_uuid();
    offer_id13 UUID := gen_random_uuid();
    offer_id14 UUID := gen_random_uuid();
    offer_id15 UUID := gen_random_uuid();
    offer_id16 UUID := gen_random_uuid();
    offer_id17 UUID := gen_random_uuid();
    offer_id18 UUID := gen_random_uuid();
    offer_id19 UUID := gen_random_uuid();
    offer_id20 UUID := gen_random_uuid();

    -- Get user UUIDs
    admin_id UUID;
    dealer_id UUID;
BEGIN
    -- Get user IDs
SELECT id INTO admin_id FROM app_users WHERE email = 'mir@koda.com';
SELECT id INTO dealer_id FROM app_users WHERE email = 'slavdeal@koda.com';

-- Insert into car_equipment table
INSERT INTO car_equipment (
    id,
    air_conditioning,
    automatic_climate,
    heated_seats,
    electric_seats,
    leather_seats,
    panoramic_roof,
    electric_windows,
    electric_mirrors,
    keyless_entry,
    wheel_heating,
    navigation_system,
    bluetooth,
    usb_port,
    multifunction,
    android_auto,
    apple_car_play,
    sound_system,
    parking_sensors,
    rear_camera,
    cruise_control,
    adaptive_cruise_control,
    lane_assist,
    blind_spot_detection,
    emergency_braking,
    start_stop,
    xenon_lights,
    led_lights,
    ambient_lighting,
    automatic_lights,
    adaptive_lights,
    heated_steering_wheel,
    electric_trunk,
    electric_sun_blind,
    head_up_display,
    aromatherapy
)
VALUES
    -- Audi A4 Avant - premium wagon with good equipment
    (car_equipment_id8,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, FALSE),

    -- Mercedes C220d AMG - luxury equipment
    (car_equipment_id9,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE),

    -- Porsche 911 Turbo S - sports coupe with top equipment
    (car_equipment_id10,
     TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, FALSE),

    -- Audi RSQ8 - performance SUV with full equipment
    (car_equipment_id11,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE),

    -- Audi Q5 S-line - premium SUV
    (car_equipment_id12,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, FALSE),

    -- BMW X3 M40i - sporty SUV
    (car_equipment_id13,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, FALSE),

    -- Mercedes GLA AMG - compact premium SUV
    (car_equipment_id14,
     TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, TRUE),

    -- Volvo XC60 T8 - safe SUV with hybrid
    (car_equipment_id15,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, FALSE),

    -- Lexus RX450h - luxury hybrid SUV
    (car_equipment_id16,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE),

    -- Jaguar F-Type - sports coupe
    (car_equipment_id17,
     TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, FALSE),

    -- Mini Cooper S - compact premium hot-hatch
    (car_equipment_id18,
     TRUE, TRUE, TRUE, FALSE, FALSE, TRUE, TRUE, TRUE, TRUE, FALSE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, FALSE,
     TRUE, FALSE, FALSE, FALSE, FALSE),

    -- Alfa Romeo Giulia QV - sports sedan
    (car_equipment_id19,
     TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, FALSE),

    -- Toyota Yaris GR - rally-bred hot hatch
    (car_equipment_id20,
     TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, TRUE, TRUE, TRUE, FALSE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, FALSE,
     TRUE, FALSE, FALSE, FALSE, FALSE);

-- Insert into car_details table
INSERT INTO car_details (id, brand, model, year, color, engine_displacement, vin_number, mileage, fuel_type, transmission, body_type, drive_type, engine_power, doors, seats, condition, registration_number, registration_country, first_owner, accident_free, service_history, additional_features, car_equipment_id)
VALUES
    (car_details_id8, 'Audi', 'A4 Avant', 2021, 'Szary', '2.0L', 'WAUZZZF44MA012345', 28000, 'PETROL', 'AUTOMATIC', 'STATION_WAGON', 'AWD', 190, 5, 5, 'USED', 'WA98765', 'PL', TRUE, TRUE, TRUE, 'Virtual Cockpit, Matrix LED, Pakiet S-Line, Quattro', car_equipment_id8),
    (car_details_id9, 'Mercedes', 'C220d AMG', 2022, 'Czarny', '2.0L', 'WDD2050291A123456', 18000, 'DIESEL', 'AUTOMATIC', 'SEDAN', 'RWD', 194, 4, 5, 'USED', 'KR11223', 'PL', TRUE, TRUE, TRUE, 'Pakiet AMG, MBUX, Asystent parkowania, Multibeam LED', car_equipment_id9),
    (car_details_id10, 'Porsche', '911 Turbo S', 2020, 'Czerwony', '3.8L', 'WP0ZZZ99ZLS234567', 15000, 'PETROL', 'AUTOMATIC', 'COUPE', 'AWD', 650, 2, 4, 'USED', 'GD99887', 'PL', FALSE, TRUE, TRUE, 'Sport Chrono, PASM, Launch Control, Sport Design, Ceramika', car_equipment_id10),
    (car_details_id11, 'Audi', 'RSQ8', 2022, 'Czarny', '4.0L', 'WAUZZZ4M2PD345678', 12000, 'PETROL', 'AUTOMATIC', 'SUV', 'AWD', 600, 5, 5, 'USED', 'WA12345', 'PL', TRUE, TRUE, TRUE, 'RS Performance, Virtual Cockpit Plus, Matrix LED, Sport Differential', car_equipment_id11),
    (car_details_id12, 'Audi', 'Q5 S-line', 2021, 'Niebieski', '2.0L', 'WAUZZZFY2N2456789', 35000, 'PETROL', 'AUTOMATIC', 'SUV', 'AWD', 265, 5, 5, 'USED', 'PO77665', 'PL', TRUE, TRUE, TRUE, 'Virtual Cockpit Plus, Matrix LED, Air Suspension, Panorama', car_equipment_id12),
    (car_details_id13, 'BMW', 'X3 M40i', 2020, 'Szary', '3.0L', 'WBXB1BZ09L2567890', 42000, 'PETROL', 'AUTOMATIC', 'SUV', 'AWD', 360, 5, 5, 'USED', 'SL54433', 'PL', TRUE, FALSE, TRUE, 'M Sport, Driving Assistant, HUD, Harman Kardon, Panorama', car_equipment_id13),
    (car_details_id14, 'Mercedes', 'GLA 35 AMG', 2022, 'Żółty', '2.0L', 'WDC1560471A678901', 12000, 'PETROL', 'AUTOMATIC', 'SUV', 'AWD', 306, 5, 5, 'USED', 'WA22334', 'PL', TRUE, TRUE, TRUE, 'AMG Performance, MBUX, Burmester, AMG Ride Control', car_equipment_id14),
    (car_details_id15, 'Volvo', 'XC60 T8', 2021, 'Biały', '2.0L', 'YV4A22RK9M1789012', 22000, 'HYBRID', 'AUTOMATIC', 'SUV', 'AWD', 390, 5, 5, 'USED', 'GD88776', 'PL', TRUE, TRUE, TRUE, 'Pilot Assist, Air Suspension, Bowers & Wilkins, Crystal Gear', car_equipment_id15),
    (car_details_id16, 'Lexus', 'RX450h', 2020, 'Szary', '3.5L', 'JTJBC1BA8L2890123', 38000, 'HYBRID', 'AUTOMATIC', 'SUV', 'AWD', 313, 5, 7, 'USED', 'KR66554', 'PL', FALSE, TRUE, TRUE, 'LSS+, Premium Audio, HUD, Panorama, E-Four AWD', car_equipment_id16),
    (car_details_id17, 'Jaguar', 'F-Type R', 2019, 'Niebieski', '5.0L', 'SAJWA6FT1KMK01234', 25000, 'PETROL', 'AUTOMATIC', 'COUPE', 'AWD', 550, 2, 2, 'USED', 'PO33221', 'PL', TRUE, TRUE, TRUE, 'SVR Kit, Meridian Audio, Active Exhaust, Carbon Fiber', car_equipment_id17),
    (car_details_id18, 'Mini', 'Cooper S', 2022, 'Zielony', '2.0L', 'WMWXS7C50N0X12345', 15000, 'PETROL', 'AUTOMATIC', 'HATCHBACK', 'FWD', 192, 3, 4, 'USED', 'SL99887', 'PL', TRUE, TRUE, TRUE, 'JCW Kit, Harman Kardon, Head-Up Display, LED Matrix', car_equipment_id18),
    (car_details_id19, 'Alfa Romeo', 'Giulia QV', 2020, 'Czerwony', '2.9L', 'ZAR95200005123456', 32000, 'PETROL', 'AUTOMATIC', 'SEDAN', 'RWD', 510, 4, 5, 'USED', 'WA77889', 'PL', TRUE, TRUE, TRUE, 'QV Performance, Brembo Carbon, Sparco Seats, Race Mode', car_equipment_id19),
    (car_details_id20, 'Toyota', 'Yaris GR', 2023, 'Biały', '1.6L', 'JTDBJ3FH50D234567', 8500, 'PETROL', 'MANUAL', 'HATCHBACK', 'AWD', 261, 3, 4, 'USED', 'GR12345', 'PL', TRUE, TRUE, TRUE, 'GR-Four AWD, Torsen LSDs, Rally suspension, GR Circuit Pack', car_equipment_id20);

-- Insert into offers table
INSERT INTO offers (id, title, description, car_details_id, price, currency, app_user_id, location, contact_phone, contact_email, created_at, updated_at, expiration_date, view_count, featured, negotiable, version)
VALUES
    (offer_id8, 'Audi A4 Avant 2.0 TFSI S-Line Quattro', 'Audi A4 Avant w sportowej wersji S-Line z napędem Quattro. Samochód w doskonałym stanie, regularnie serwisowany w ASO. Pierwszy właściciel, bezwypadkowy. Virtual Cockpit, Matrix LED, skórzana tapicerka. Idealne kombi dla rodziny.', car_details_id8, 145000, 'PLN', admin_id, 'Warszawa', '111333555', 'audi.a4@example.com', NOW(), NOW(), '2025-12-31', 0, FALSE, TRUE, 1),

    (offer_id9, 'Mercedes C220d AMG Line - Diesel Premium', 'Mercedes C-Class w pakiecie AMG Line z ekonomicznym silnikiem diesel. Samochód jak nowy, bogato wyposażony. System MBUX, asystenci jazdy, multibeam LED. Pierwszy właściciel, pełna historia serwisowa w ASO Mercedes.', car_details_id9, 165000, 'PLN', admin_id, 'Kraków', '222444666', 'mercedes.c@example.com', NOW(), NOW(), '2025-11-30', 0, TRUE, FALSE, 1),

    (offer_id10, 'Porsche 911 Turbo S 650KM - Ikona motoryzacji', 'Porsche 911 Turbo S - absolutny szczyt osiągnięć w klasie sportowych coupe. 650KM mocy, napęd AWD, launch control. Samochód w idealnym stanie, drugi właściciel. Pełna historia serwisowa w ASO Porsche. Hamulce ceramiczne, Sport Chrono.', car_details_id10, 750000, 'PLN', dealer_id, 'Wrocław', '333555777', 'porsche911@example.com', NOW(), NOW(), '2025-12-15', 0, TRUE, TRUE, 1),

    (offer_id11, 'Audi RSQ8 4.0 TFSI Quattro 600KM - Performance SUV', 'Audi RSQ8 - najszybszy SUV w gamie Audi z silnikiem V8 4.0 TFSI o mocy 600KM. Samochód w idealnym stanie, niski przebieg. RS Performance pakiet, Virtual Cockpit Plus, Matrix LED. Pierwszy właściciel, serwisowany wyłącznie w ASO Audi.', car_details_id11, 485000, 'PLN', admin_id, 'Gdańsk', '444666888', 'audi.rsq8@example.com', NOW(), NOW(), '2025-10-30', 0, TRUE, FALSE, 1),

    (offer_id12, 'Audi Q5 S-Line Quattro - Premium SUV', 'Audi Q5 w sportowej wersji S-Line z napędem Quattro. Doskonały SUV klasy premium z bogatym wyposażeniem. Virtual Cockpit Plus, Matrix LED, zawieszenie pneumatyczne. Pierwszy właściciel, bezwypadkowy, serwisowany w ASO.', car_details_id12, 175000, 'PLN', admin_id, 'Poznań', '555777999', 'audi.q5@example.com', NOW(), NOW(), '2025-11-15', 0, FALSE, TRUE, 1),

    (offer_id13, 'BMW X3 M40i xDrive - Sportowy SUV z charakterem', 'BMW X3 M40i z mocnym silnikiem 3.0L i 360KM mocy. Pakiet M Sport, system xDrive, asystenci jazdy. Samochód w bardzo dobrym stanie, pierwszy właściciel. Panoramiczny dach, HUD, Harman Kardon. Idealny dla aktywnych.', car_details_id13, 155000, 'PLN', dealer_id, 'Łódź', '666888000', 'bmw.x3@example.com', NOW(), NOW(), '2025-12-01', 0, TRUE, FALSE, 1),

    (offer_id14, 'Mercedes GLA 35 AMG 4Matic - Kompaktowy SUV z mocą', 'Mercedes GLA w topowej wersji AMG 35 z napędem 4Matic. 306KM mocy w kompaktowej bryle. Samochód w idealnym stanie, pierwszy właściciel. System MBUX, Burmester audio, AMG Ride Control. Wyróżnia się sportowym charakterem.', car_details_id14, 185000, 'PLN', dealer_id, 'Wrocław', '777999111', 'mercedes.gla@example.com', NOW(), NOW(), '2025-11-20', 0, TRUE, TRUE, 1),

    (offer_id15, 'Volvo XC60 T8 Polestar - Hybrydowy SUV z mocą', 'Volvo XC60 T8 Polestar z napędem hybrydowym plug-in i mocą 390KM. Najbezpieczniejszy SUV na rynku z systemem Pilot Assist. Zawieszenie pneumatyczne, audio Bowers & Wilkins. Pierwszy właściciel, serwisowany w ASO.', car_details_id15, 205000, 'PLN', admin_id, 'Kraków', '888000222', 'volvo.xc60@example.com', NOW(), NOW(), '2025-10-31', 0, FALSE, FALSE, 1),

    (offer_id16, 'Lexus RX450h AWD - Luksusowy hybrydowy SUV', 'Lexus RX450h z napędem hybrydowym i systemem AWD. Samochód klasy premium z niezawodnym napędem hybrydowym. System LSS+, audio premium, HUD. Drugi właściciel, pełna dokumentacja serwisowa. 7 miejsc, ideał dla dużej rodziny.', car_details_id16, 165000, 'PLN', admin_id, 'Warszawa', '999111333', 'lexus.rx@example.com', NOW(), NOW(), '2025-12-10', 0, FALSE, TRUE, 1),

    (offer_id17, 'Jaguar F-Type R AWD 550KM - Brytyjska elegancja sportowa', 'Jaguar F-Type R z mocnym silnikiem V8 5.0L i mocą 550KM. Sportowe coupe z napędem AWD. Samochód w doskonałym stanie, pierwszy właściciel. Audio Meridian, aktywny wydech, carbon fiber. Prawdziwa gratka dla kolekcjonerów.', car_details_id17, 295000, 'PLN', dealer_id, 'Gdańsk', '000222444', 'jaguar.ftype@example.com', NOW(), NOW(), '2025-11-25', 0, TRUE, TRUE, 1),

    (offer_id18, 'Mini Cooper S JCW - Najmocniejszy Mini na rynku', 'Mini Cooper S w wersji John Cooper Works z mocą 192KM. Kompaktowy hot-hatch z charakterem. Samochód w idealnym stanie, pierwszy właściciel. HUD, Matrix LED, Harman Kardon. Doskonały na miasto i weekend za miastem.', car_details_id18, 125000, 'PLN', admin_id, 'Poznań', '111444777', 'mini.cooper@example.com', NOW(), NOW(), '2025-10-28', 0, FALSE, FALSE, 1),

    (offer_id19, 'Alfa Romeo Giulia Quadrifoglio 510KM - Włoska pasja', 'Alfa Romeo Giulia Quadrifoglio z silnikiem V6 2.9L BiTurbo o mocy 510KM. Najbardziej emocjonalna limuzyna na rynku. Fotele Sparco, hamulce Brembo Carbon, tryb Race. Pierwszy właściciel, serwisowana w ASO. Dla prawdziwych pasjonatów.', car_details_id19, 225000, 'PLN', dealer_id, 'Łódź', '222555888', 'alfa.giulia@example.com', NOW(), NOW(), '2025-12-05', 0, TRUE, FALSE, 1),

    (offer_id20, 'Toyota Yaris GR 1.6 Turbo AWD 261KM - Rally Legend', 'Toyota Yaris GR - jedyny prawdziwy hot-hatch z napędem AWD. Silnik 1.6L turbo z 261KM, system GR-Four AWD, blokady Torsen. Samochód w idealnym stanie, pierwszy właściciel. Stworzony do rajdów, doskonały na co dzień. Prawdziwa gratka dla fanów motorsportu.', car_details_id20, 145000, 'PLN', admin_id, 'Wrocław', '333666999', 'toyota.yaris.gr@example.com', NOW(), NOW(), '2025-11-12', 0, TRUE, TRUE, 1);

-- Insert into offer_images table
INSERT INTO offer_images (id, url, caption, is_primary, sort_order, offer_id)
VALUES
    -- Audi A4 Avant
    (gen_random_uuid(), 'audi_a4_avant.png', 'Audi A4 Avant S-Line', TRUE, 1, offer_id8),
    (gen_random_uuid(), 'audi_a4_interior.png', 'Audi A4 interior with Virtual Cockpit', FALSE, 2, offer_id8),

    -- Mercedes C220d AMG
    (gen_random_uuid(), 'mercedes_c220d.png', 'Mercedes C220d AMG Line', TRUE, 1, offer_id9),
    (gen_random_uuid(), 'mercedes_c220d_rear.png', 'Mercedes C-Class AMG rear', FALSE, 2, offer_id9),

    -- Porsche 911 Turbo S
    (gen_random_uuid(), 'porsche_911_turbo.png', 'Porsche 911 Turbo S', TRUE, 1, offer_id10),
    (gen_random_uuid(), 'porsche_911_rear.png', 'Porsche 911 Turbo S Rear', FALSE, 2, offer_id10),

    -- Audi RSQ8
    (gen_random_uuid(), 'audi_rsq8.png', 'Audi RSQ8 600KM', TRUE, 1, offer_id11),
    (gen_random_uuid(), 'audi_rsq8_rear.png', 'Audi RSQ8 sports rear', FALSE, 2, offer_id11),

    -- Audi Q5 S-line
    (gen_random_uuid(), 'audi_q5_sline.png', 'Audi Q5 S-Line Quattro', TRUE, 1, offer_id12),
    (gen_random_uuid(), 'audi_q5_rear.png', 'Audi Q5 S-Line rear', FALSE, 2, offer_id12),

    -- BMW X3 M40i
    (gen_random_uuid(), 'bmw_x3_m40i.png', 'BMW X3 M40i xDrive', TRUE, 1, offer_id13),
    (gen_random_uuid(), 'bmw_x3_rear.png', 'BMW X3 M40i sports rear', FALSE, 2, offer_id13),

    -- Mercedes GLA AMG
    (gen_random_uuid(), 'mercedes_gla_amg.png', 'Mercedes GLA 35 AMG', TRUE, 1, offer_id14),
    (gen_random_uuid(), 'mercedes_gla_rear.png', 'Mercedes GLA AMG rear', FALSE, 2, offer_id14),

    -- Volvo XC60 T8
    (gen_random_uuid(), 'volvo_xc60_t8.png', 'Volvo XC60 T8 Polestar', TRUE, 1, offer_id15),
    (gen_random_uuid(), 'volvo_xc60_rear.png', 'Volvo XC60 Scandinavian rear', FALSE, 2, offer_id15),

    -- Lexus RX450h
    (gen_random_uuid(), 'lexus_rx450h.png', 'Lexus RX450h AWD', TRUE, 1, offer_id16),
    (gen_random_uuid(), 'lexus_rx450h_rear.png', 'Lexus RX luxury rear', FALSE, 2, offer_id16),

    -- Jaguar F-Type
    (gen_random_uuid(), 'jaguar_ftype.png', 'Jaguar F-Type R AWD', TRUE, 1, offer_id17),
    (gen_random_uuid(), 'jaguar_ftype_rear.png', 'Jaguar F-Type sports rear', FALSE, 2, offer_id17),

    -- Mini Cooper S
    (gen_random_uuid(), 'mini_cooper_s.png', 'Mini Cooper S JCW', TRUE, 1, offer_id18),
    (gen_random_uuid(), 'mini_cooper_rear.png', 'Mini characteristic rear', FALSE, 2, offer_id18),

    -- Alfa Romeo Giulia QV
    (gen_random_uuid(), 'alfa_giulia_qv.png', 'Alfa Romeo Giulia Quadrifoglio', TRUE, 1, offer_id19),
    (gen_random_uuid(), 'alfa_giulia_rear.png', 'Alfa Romeo Giulia QV sports rear', FALSE, 2, offer_id19),

    -- Toyota Yaris GR
    (gen_random_uuid(), 'toyota_yaris_gr.png', 'Toyota Yaris GR Rally Edition', TRUE, 1, offer_id20);

END $$;