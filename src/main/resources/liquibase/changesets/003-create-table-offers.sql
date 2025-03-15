--liquibase formatted sql
--changeset s25323:003-create-table-offer

CREATE TABLE offers
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version         INTEGER,
    title           VARCHAR(255),
    description     VARCHAR(2000),
    car_details_id  UUID UNIQUE,
    price           NUMERIC(19, 2),
    currency        VARCHAR(10),
    location        VARCHAR(255),
    contact_phone   VARCHAR(50),
    contact_email   VARCHAR(255),
    created_at      TIMESTAMP        DEFAULT NOW(),
    updated_at      TIMESTAMP        DEFAULT NOW(),
    expiration_date TIMESTAMP,
    view_count      INTEGER,
    featured        BOOLEAN          DEFAULT FALSE,
    negotiable      BOOLEAN          DEFAULT TRUE,
    CONSTRAINT fk_car_details FOREIGN KEY (car_details_id) REFERENCES car_details (id)
);
