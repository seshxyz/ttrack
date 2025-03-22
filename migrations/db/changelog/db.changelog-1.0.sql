--liquibase formatted sql

--changeset seshxyz:1
CREATE TABLE IF NOT EXISTS tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(540) NOT NULL,
    priority VARCHAR(13) NOT NULL,
    status VARCHAR(13) NOT NULL,
    state VARCHAR(13) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE
);
