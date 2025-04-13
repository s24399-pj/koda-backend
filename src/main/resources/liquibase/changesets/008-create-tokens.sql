--changeset alopalka:008-create-tokens
--liquibase formatted sql

CREATE TABLE token
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    token_value VARCHAR(1024) NOT NULL UNIQUE,
    token_type  VARCHAR(50) NOT NULL DEFAULT 'BEARER',
    app_user_id UUID NOT NULL,
    CONSTRAINT fk_token_app_user FOREIGN KEY (app_user_id) REFERENCES app_users (id)
);

CREATE INDEX idx_token_app_user_id ON token (app_user_id);