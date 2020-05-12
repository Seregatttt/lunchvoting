DROP TABLE IF EXISTS user_lunch;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS restaurants;


DROP SEQUENCE IF EXISTS global_seq_users;
DROP SEQUENCE IF EXISTS global_seq_rest;

CREATE SEQUENCE global_seq_users START WITH 100;

CREATE TABLE users
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq_users'),
    name             VARCHAR                 NOT NULL,
    email            VARCHAR                 NOT NULL,
    password         VARCHAR                 NOT NULL
);

CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER                        NOT NULL,
    role    VARCHAR,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE SEQUENCE global_seq_rest START WITH 10;

CREATE TABLE restaurants
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq_rest'),
    name             VARCHAR                 NOT NULL,
    address          VARCHAR                 NOT NULL

);

CREATE TABLE menu
(
    rest_id          INTEGER                 NOT NULL,
    date_menu        TIMESTAMP 	           NOT NULL,
    name_meal        VARCHAR                 NOT NULL,
    cost_meal        INTEGER                 NOT NULL,
    FOREIGN KEY (rest_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

CREATE TABLE user_lunch
(
    user_id          INTEGER                 NOT NULL,
    rest_id          INTEGER                 NOT NULL,
    date_time        TIMESTAMP               NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (rest_id) REFERENCES restaurants (id) ON DELETE CASCADE
);