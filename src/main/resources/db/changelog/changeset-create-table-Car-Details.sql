--liquibase formatted sql
--changeset s25323-pj:create-table-Car-Details

CREATE TABLE car_details
(
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    brand                VARCHAR(255),
    model                VARCHAR(255),
    year                 INTEGER,
    color                VARCHAR(255),
    engine_displacement  VARCHAR(255),
    vin_number           VARCHAR(255) UNIQUE,
    mileage              INTEGER,
    fuel_type            VARCHAR(50),
    transmission         VARCHAR(50),
    body_type            VARCHAR(50),
    drive_type           VARCHAR(50),
    engine_power         INTEGER,
    doors                INTEGER,
    seats                INTEGER,
    condition            VARCHAR(50),
    registration_number  VARCHAR(255),
    registration_country VARCHAR(255),
    first_owner          BOOLEAN,
    accident_free        BOOLEAN,
    service_history      BOOLEAN,
    additional_features  VARCHAR(1000)
);
