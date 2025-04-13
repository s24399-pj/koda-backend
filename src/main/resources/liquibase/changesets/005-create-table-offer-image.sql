--liquibase formatted sql
--changeset s25323-pj:006-create-table-offer-image

CREATE TABLE offer_images
(
    id         UUID PRIMARY KEY,
    url        VARCHAR(255) NOT NULL,
    caption    VARCHAR(255),
    is_primary BOOLEAN DEFAULT FALSE,
    sort_order INT,
    offer_id   UUID,
    CONSTRAINT fk_offer_images_offer FOREIGN KEY (offer_id) REFERENCES offers (id)
);