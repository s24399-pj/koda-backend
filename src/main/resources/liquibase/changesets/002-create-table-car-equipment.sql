--changeset s25323-pj:002-create-table-car-equipment
--liquibase formatted sql

CREATE TABLE car_equipment
(
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    car_details_id          UUID UNIQUE,

    -- Comfort
    air_conditioning        BOOLEAN,
    automatic_climate       BOOLEAN,
    heated_seats            BOOLEAN,
    electric_seats          BOOLEAN,
    leather_seats           BOOLEAN,
    panoramic_roof          BOOLEAN,
    electric_windows        BOOLEAN,
    electric_mirrors        BOOLEAN,
    keyless_entry           BOOLEAN,
    wheel_heating           BOOLEAN,

    -- Multimedia
    navigation_system       BOOLEAN,
    bluetooth               BOOLEAN,
    usb_port                BOOLEAN,
    multifunction           BOOLEAN,
    android_auto            BOOLEAN,
    apple_car_play          BOOLEAN,
    sound_system            BOOLEAN,

    -- Assistance systems
    parking_sensors         BOOLEAN,
    rear_camera             BOOLEAN,
    cruise_control          BOOLEAN,
    adaptive_cruise_control BOOLEAN,
    lane_assist             BOOLEAN,
    blind_spot_detection    BOOLEAN,
    emergency_braking       BOOLEAN,
    start_stop              BOOLEAN,

    -- Lighting
    xenon_lights            BOOLEAN,
    led_lights              BOOLEAN,
    ambient_lighting        BOOLEAN,
    automatic_lights        BOOLEAN,
    adaptive_lights         BOOLEAN,

    -- Additional features
    heated_steering_wheel   BOOLEAN,
    electric_trunk          BOOLEAN,
    electric_sun_blind      BOOLEAN,
    head_up_display         BOOLEAN,
    aromatherapy            BOOLEAN,

    CONSTRAINT fk_car_details FOREIGN KEY (car_details_id) REFERENCES car_details (id)
);