--liquibase formatted sql
--changeset s25323-pj:create-table-Offer-Image

CREATE TABLE offer_images
(
    id         UUID PRIMARY KEY,
    url        VARCHAR(255) NOT NULL,
    caption    VARCHAR(255),
    primary BOOLEAN DEFAULT FALSE,
    sort_order INT
);
