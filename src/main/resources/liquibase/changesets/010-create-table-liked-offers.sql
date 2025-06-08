-- changeset s25323:010-create-table-liked-offers
-- comment Generate offers

CREATE TABLE liked_offers (
                              id UUID PRIMARY KEY,
                              user_id UUID NOT NULL,
                              offer_id UUID NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_liked_offers_user FOREIGN KEY (user_id) REFERENCES app_users(id),
                              CONSTRAINT fk_liked_offers_offer FOREIGN KEY (offer_id) REFERENCES offers(id),
                              CONSTRAINT uk_liked_offers_user_offer UNIQUE (user_id, offer_id)
);