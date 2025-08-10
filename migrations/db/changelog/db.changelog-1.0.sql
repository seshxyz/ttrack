--liquibase formatted sql

--changeset seshxyz:1
--comment: 04.03.2025 0:53
--comment: Created base schema
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    authorities TEXT[] NOT NULL,
    last_login TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tasks (
    id VARCHAR PRIMARY KEY,
    title VARCHAR(255) UNIQUE NOT NULL,
    description VARCHAR(540) NOT NULL,
    priority VARCHAR(13) NOT NULL,
    status VARCHAR(13) NOT NULL,
    state INT NOT NULL,
    is_completed BOOLEAN NOT NULL DEFAULT FALSE,
    owner VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),

    FOREIGN KEY (owner) REFERENCES users (username)
);

INSERT INTO users (id, username, password, is_active, authorities)
    VALUES ('usr_a4c52bfb-7a21-4f95-911f-073bde69eb89',
            'admin',
            '$2a$12$W3zVbxi2Wh766c5oqlHuVeCg91owirbPazdRGLadymxYNO9diM4cS',
            true,
            '{"ROLE_BASE_USER,ROLE_ADMIN"}'),
            ('usr_21a4c52b-7a21-4f95-911f-073bde69eb89',
             'test',
             '$2a$12$W3zVbxi2Wh766c5oqlHuVeCg91owirbPazdRGLadymxYNO9diM4cS',
             true,
             '{"ROLE_BASE_USER"}');


--changeset seshxyz:2
--comment: 04.08.2025 20:21
ALTER TABLE tasks RENAME column description to details;
ALTER TABLE tasks DROP CONSTRAINT tasks_title_key,
    ALTER COLUMN title DROP NOT NULL,
    ALTER COLUMN details DROP NOT NULL,
    ALTER COLUMN state SET DATA TYPE VARCHAR;
TRUNCATE users CASCADE;


--changeset seshxyz:3
--comment: 04.08.2025 20:51
CREATE TABLE permission (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE user_permission (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR NOT NULL,
    permission_id SMALLSERIAL NOT NULL,
    assigned_by VARCHAR NOT NULL,
    assigned_at TIMESTAMP NOT NULL,

    FOREIGN KEY (user_name) REFERENCES users (username),
    FOREIGN KEY (permission_id) REFERENCES permission (id)
);

ALTER TABLE users DROP COLUMN authorities;

INSERT INTO permission (name) VALUES ('ROLE_BASE_USER'), ('ROLE_ADMIN');

INSERT INTO users (id, username, password, is_active)
VALUES ('usr_a4c52bfb-7a21-4f95-911f-073bde69eb89',
        'admin',
        '$2a$12$W3zVbxi2Wh766c5oqlHuVeCg91owirbPazdRGLadymxYNO9diM4cS',
        true),
       ('usr_21a4c52b-7a21-4f95-911f-073bde69eb89',
        'test',
        '$2a$12$W3zVbxi2Wh766c5oqlHuVeCg91owirbPazdRGLadymxYNO9diM4cS',
        true);

INSERT INTO user_permission (user_name, permission_id, assigned_by, assigned_at)
    VALUES ('admin',1,'admin',current_timestamp),
           ('admin',2, 'admin', current_timestamp),
           ('test',1,'test',current_timestamp);

CREATE INDEX ON tasks(owner);
CREATE INDEX ON user_permission(user_name);
CREATE INDEX ON user_permission(permission_id)