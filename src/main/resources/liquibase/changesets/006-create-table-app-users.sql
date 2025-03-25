--changeset alopalka:006-create-table-app-users
--liquibase formatted sql

CREATE TABLE app_users
(
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    profile_picture BYTEA,
    locked          BOOLEAN DEFAULT FALSE NOT NULL,
    enabled         BOOLEAN DEFAULT FALSE NOT NULL,
    version         INTEGER DEFAULT 0 NOT NULL,
    role            VARCHAR(50) NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);