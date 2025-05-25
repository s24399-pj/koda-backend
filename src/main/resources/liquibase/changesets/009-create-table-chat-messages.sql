--liquibase formatted sql
--changeset s24399:009-create-table-chat-messages

CREATE TABLE chat_messages
(
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_id    UUID,
    recipient_id UUID,
    content      TEXT NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    status       VARCHAR(50) NOT NULL,
    version      INTEGER NOT NULL DEFAULT 0,
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES app_users (id),
    CONSTRAINT fk_recipient FOREIGN KEY (recipient_id) REFERENCES app_users (id)
);