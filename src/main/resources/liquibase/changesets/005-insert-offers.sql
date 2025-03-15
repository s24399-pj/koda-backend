-- changeset alopalka:005-insert-offers
-- comment Generate offers

-- Deklaracja zmiennych do przechowywania wygenerowanych UUID
DO $$
DECLARE
car_equipment_id1 UUID := gen_random_uuid();
    car_equipment_id2 UUID := gen_random_uuid();
    car_equipment_id3 UUID := gen_random_uuid();
    car_equipment_id4 UUID := gen_random_uuid();
    car_equipment_id5 UUID := gen_random_uuid();
    car_equipment_id6 UUID := gen_random_uuid();
    car_equipment_id7 UUID := gen_random_uuid();

    car_details_id1 UUID := gen_random_uuid();
    car_details_id2 UUID := gen_random_uuid();
    car_details_id3 UUID := gen_random_uuid();
    car_details_id4 UUID := gen_random_uuid();
    car_details_id5 UUID := gen_random_uuid();
    car_details_id6 UUID := gen_random_uuid();
    car_details_id7 UUID := gen_random_uuid();

    offer_id1 UUID := gen_random_uuid();
    offer_id2 UUID := gen_random_uuid();
    offer_id3 UUID := gen_random_uuid();
    offer_id4 UUID := gen_random_uuid();
    offer_id5 UUID := gen_random_uuid();
    offer_id6 UUID := gen_random_uuid();
    offer_id7 UUID := gen_random_uuid();
BEGIN

-- Insert do tabeli car_equipment
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
    -- Toyota Corolla - podstawowe, ale dobre wyposażenie rodzinnego auta
    (car_equipment_id1,
     TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, TRUE, TRUE, TRUE, FALSE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE,
     TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, TRUE, TRUE,
     FALSE, TRUE, FALSE, TRUE, FALSE,
     FALSE, FALSE, FALSE, FALSE, FALSE),

    -- Honda Civic - nowoczesne wyposażenie klasy kompakt premium
    (car_equipment_id2,
     TRUE, TRUE, TRUE, FALSE, TRUE, FALSE, TRUE, TRUE, TRUE, FALSE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, FALSE,
     TRUE, FALSE, FALSE, FALSE, FALSE),

    -- BMW M5 F90 - topowa limuzyna z pełnym wyposażeniem
    (car_equipment_id3,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE),

    -- Ford Focus RS - hot-hatch z dobrym wyposażeniem
    (car_equipment_id4,
     TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, FALSE,
     TRUE, FALSE, FALSE, FALSE, FALSE),

    -- Skoda Octavia RS - praktyczne kombi sportowe
    (car_equipment_id5,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, FALSE),

    -- Volkswagen Golf MK8 R - nowoczesny hot-hatch premium
    (car_equipment_id6,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, FALSE, TRUE, FALSE),

    -- BMW M4 CSL - luksusowe coupe z topowym wyposażeniem
    (car_equipment_id7,
     TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE,
     FALSE, TRUE, TRUE, TRUE, TRUE,
     TRUE, TRUE, TRUE, TRUE, TRUE);

-- Insert do tabeli car_details
INSERT INTO car_details (id, brand, model, year, color, engine_displacement, vin_number, mileage, fuel_type, transmission, body_type, drive_type, engine_power, doors, seats, condition, registration_number, registration_country, first_owner, accident_free, service_history, additional_features, car_equipment_id)
VALUES
    (car_details_id1, 'Toyota', 'Corolla', 2021, 'Czarny', '2.0L', 'SB1ZB3AE60E092395', 12643, 'PETROL', 'AUTOMATIC', 'STATION_WAGON', 'FWD', 152, 4, 5, 'USED', 'RZE2136E', 'PL', TRUE, TRUE, TRUE, 'Klimatyzacja, Kamera parkingowa, Asystent pasa ruchu', car_equipment_id1),
    (car_details_id2, 'Honda', 'Civic', 2022, 'Srebrny', '1.5L', '2HGBH41JXMN109187', 5000, 'PETROL', 'AUTOMATIC', 'SEDAN', 'FWD', 150, 4, 5, 'USED', 'GA2456', 'PL', TRUE, TRUE, TRUE, 'System Honda Sensing, Adaptacyjny tempomat, Asystent martwego pola', car_equipment_id2),
    (car_details_id3, 'BMW', 'M5 F90', 2020, 'Niebieski', '4.4L', 'WBSJF0C01KBM82393', 25000, 'PETROL', 'AUTOMATIC', 'SEDAN', 'AWD', 625, 4, 5, 'USED', 'WA12345', 'PL', FALSE, TRUE, TRUE, 'System M Drive, Launch Control, Hamulce ceramiczne, Sportowy wydech M Performance, Pakiet Competition', car_equipment_id3),
    (car_details_id4, 'Ford', 'Focus RS', 2017, 'Niebieski', '2.3L', '1FAHP3F93CL429901', 45000, 'PETROL', 'MANUAL', 'HATCHBACK', 'AWD', 350, 5, 5, 'USED', 'KR98765', 'PL', TRUE, FALSE, TRUE, 'Tryb Drift, Launch Control, Kubełkowe fotele Recaro, Sportowy wydech, Zawieszenie sportowe', car_equipment_id4),
    (car_details_id5, 'Skoda', 'Octavia RS', 2019, 'Czarny', '2.0L', 'TMBEJ9NE2K0275643', 52000, 'PETROL', 'AUTOMATIC', 'STATION_WAGON', 'FWD', 245, 5, 5, 'USED', 'PO54321', 'PL', TRUE, TRUE, TRUE, 'Sportowe zawieszenie, Tryb sport, Virtual cockpit, Dynamic sound boost, Pakiet RS', car_equipment_id5),
    (car_details_id6, 'Volkswagen', 'Golf MK8 R', 2021, 'Biały', '2.0L', 'WVWZZZ1KZMP051234', 15000, 'PETROL', 'AUTOMATIC', 'HATCHBACK', 'AWD', 320, 5, 5, 'USED', 'SL76543', 'PL', TRUE, TRUE, TRUE, 'System 4Motion, Tryb Drift, Adaptacyjne zawieszenie DCC, System R-Performance, Wydech Akrapovic', car_equipment_id6),
    (car_details_id7, 'BMW', 'M4 CSL G82', 2022, 'Szary', '3.0L', 'WBS73AZ01NCG87654', 8000, 'PETROL', 'AUTOMATIC', 'COUPE', 'RWD', 550, 2, 4, 'USED', 'GD43210', 'PL', TRUE, TRUE, TRUE, 'Karbonowa maska, Karbonowe fotele, M Drive Professional, Układ wydechowy M Sport, Pakiet CSL', car_equipment_id7);

-- Insert do tabeli offer
INSERT INTO offers (id, title, description, car_details_id, price, currency, location, contact_phone, contact_email, created_at, updated_at, expiration_date, view_count, featured, negotiable, version)
VALUES
    (offer_id1, 'Toyota Corolla TS 2.0 Dynamic Force Hybrid', 'Toyota Corolla w wersji kombi z hybrydowym napędem 2.0L Dynamic Force. Samochód w bardzo dobrym stanie technicznym i wizualnym, regularnie serwisowany, bezwypadkowy. Pierwszy właściciel, komplet dokumentów serwisowych.', car_details_id1, 112000, 'PLN', 'Warszawa', '123456789', 'kontakt@toyota.pl', NOW(), NOW(), '2025-12-31', 0, FALSE, TRUE, 1),

    (offer_id2, 'Honda Civic Sedan 1.5 VTEC Turbo Executive', 'Honda Civic w najwyższej wersji wyposażenia Executive z silnikiem 1.5 VTEC Turbo. Samochód jak nowy, niski przebieg, pełna historia serwisowa ASO Honda. Idealny dla osób ceniących komfort i niezawodność.', car_details_id2, 125000, 'PLN', 'Wrocław', '987654321', 'kontakt@honda.pl', NOW(), NOW(), '2025-12-31', 0, FALSE, FALSE, 1),

    (offer_id3, 'BMW M5 Competition F90 - 625KM - Limitowana edycja', 'BMW M5 Competition w idealnym stanie. Samochód serwisowany w ASO, pełna historia serwisowa, drugi właściciel. Pakiet Competition, pełne wyposażenie. Możliwość sprawdzenia w dowolnym serwisie. Prawdziwy potwór mocy w eleganckiej limuzynie.', car_details_id3, 450000, 'PLN', 'Kraków', '555444333', 'bmw.sprzedaz@example.com', NOW(), NOW(), '2025-12-15', 0, TRUE, TRUE, 1),

    (offer_id4, 'Ford Focus RS MK3 - Kultowy hot-hatch w kolorze Nitrous Blue', 'Ford Focus RS w oryginalnym niebieskim kolorze Nitrous Blue. Samochód w pełni sprawny, regularnie serwisowany. Niewielkie ślady użytkowania. Prawdziwa gratka dla fanów szybkich hatchbacków. Auto jest wyposażone w unikatowy tryb Drift, który pozwala na kontrolowane poślizgi.', car_details_id4, 120000, 'PLN', 'Poznań', '111222333', 'focusrs@example.com', NOW(), NOW(), '2025-11-30', 0, TRUE, FALSE, 1),

    (offer_id5, 'Skoda Octavia RS 245KM - Kombi z mocą sportowego auta', 'Skoda Octavia RS w doskonałym stanie technicznym i wizualnym. Samochód regularnie serwisowany w ASO z pełną dokumentacją. Pierwszy właściciel, bezwypadkowy. Idealny dla osób szukających praktycznego auta z duszą sportowca. Virtual cockpit, automatyczna skrzynia DSG, pakiet czerni.', car_details_id5, 95000, 'PLN', 'Gdańsk', '333222111', 'octavia.rs@example.com', NOW(), NOW(), '2025-11-15', 0, FALSE, TRUE, 1),

    (offer_id6, 'Volkswagen Golf MK8 R 320KM - Najnowsza generacja ikony hot-hatchy', 'Golf R MK8 w idealnym stanie, jak nowy. Bogato wyposażony, wszystkie dostępne opcje. Auto serwisowane wyłącznie w ASO VW z pełną dokumentacją. Pierwszy właściciel, garażowany. Możliwość sprawdzenia w dowolnym serwisie. Wersja z pakietem R-Performance i wydechem Akrapovic.', car_details_id6, 180000, 'PLN', 'Łódź', '444555666', 'golf.r@example.com', NOW(), NOW(), '2025-10-30', 0, TRUE, FALSE, 1),

    (offer_id7, 'BMW M4 CSL G82 550KM - Limitowana edycja z numerem', 'Unikatowe BMW M4 CSL - jeden z 1000 wyprodukowanych egzemplarzy. Samochód kolekcjonerski, idealny stan, niski przebieg. Pełne wyposażenie z M Carbon pakietem. Historia serwisowa udokumentowana, wszystkie przeglądy w ASO BMW. Karbonowy dach, maska i zderzaki. Prawdziwe auto dla kolekcjonerów.', car_details_id7, 650000, 'PLN', 'Warszawa', '777888999', 'm4csl@example.com', NOW(), NOW(), '2025-12-20', 0, TRUE, TRUE, 1);

-- Insert do tabeli offer_images
INSERT INTO offer_images (id, url, caption, is_primary, sort_order, offer_id)
VALUES
    (gen_random_uuid(), 'toyota.webp', 'Toyota Corolla TS Hybrid', TRUE, 1, offer_id1),
    (gen_random_uuid(), 'toyota_interior.webp', 'Wnętrze Toyoty Corolla', FALSE, 2, offer_id1),
    (gen_random_uuid(), 'toyota_trunk.webp', 'Bagażnik Toyoty Corolla TS', FALSE, 3, offer_id1),

    (gen_random_uuid(), 'honda.webp', 'Honda Civic Sedan', TRUE, 1, offer_id2),
    (gen_random_uuid(), 'honda_interior.webp', 'Wnętrze Hondy Civic', FALSE, 2, offer_id2),
    (gen_random_uuid(), 'honda_rear.webp', 'Tył Hondy Civic', FALSE, 3, offer_id2),

    (gen_random_uuid(), 'bmw_m5.png', 'BMW M5 F90 Competition', TRUE, 1, offer_id3),
    (gen_random_uuid(), 'bmw_m5_interior.png', 'Wnętrze BMW M5', FALSE, 2, offer_id3),
    (gen_random_uuid(), 'bmw_m5_engine.png', 'Silnik V8 4.4L Twin-Turbo', FALSE, 3, offer_id3),
    (gen_random_uuid(), 'bmw_m5_wheels.webp', 'Felgi BMW M5 Competition', FALSE, 4, offer_id3),
    (gen_random_uuid(), 'bmw_m5_rear.png', 'Tył BMW M5 Competition', FALSE, 5, offer_id3),

    (gen_random_uuid(), 'ford_focus_rs.webp', 'Ford Focus RS', TRUE, 1, offer_id4),
    (gen_random_uuid(), 'ford_focus_rs_rear.webp', 'Ford Focus RS - tył', FALSE, 2, offer_id4),
    (gen_random_uuid(), 'ford_focus_rs_interior.webp', 'Wnętrze Forda Focus RS', FALSE, 3, offer_id4),

    (gen_random_uuid(), 'skoda_octavia_rs.webp', 'Skoda Octavia RS', TRUE, 1, offer_id5),
    (gen_random_uuid(), 'skoda_octavia_rs_interior.webp', 'Wnętrze Skody Octavia RS', FALSE, 2, offer_id5),
    (gen_random_uuid(), 'skoda_octavia_rs_rear.webp', 'Tył Skody Octavia RS', FALSE, 3, offer_id5),

    (gen_random_uuid(), 'vw_golf_r.webp', 'Volkswagen Golf MK8 R', TRUE, 1, offer_id6),
    (gen_random_uuid(), 'vw_golf_r_rear.webp', 'Volkswagen Golf R - tył', FALSE, 2, offer_id6),

    (gen_random_uuid(), 'bmw_m4_csl.webp', 'BMW M4 CSL G82', TRUE, 1, offer_id7),
    (gen_random_uuid(), 'bmw_m4_csl_interior.webp', 'Wnętrze BMW M4 CSL', FALSE, 2, offer_id7),
    (gen_random_uuid(), 'bmw_m4_csl_rear.webp', 'BMW M4 CSL - tył', FALSE, 3, offer_id7);

END $$;