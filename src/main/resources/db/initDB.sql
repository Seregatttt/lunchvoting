DROP TABLE IF EXISTS votes;
DROP TABLE IF EXISTS user_lunch;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS meals;
DROP TABLE IF EXISTS menus;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;

DROP SEQUENCE IF EXISTS global_seq_users;
DROP SEQUENCE IF EXISTS global_seq_rest;
DROP SEQUENCE IF EXISTS global_seq_menus;
DROP SEQUENCE IF EXISTS global_seq_meals;

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

CREATE SEQUENCE global_seq_menus START WITH 10000;

CREATE TABLE menus
(   id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq_menus'),
    rest_id          INTEGER                 NOT NULL,
    date_menu        date   	             NOT NULL,
    FOREIGN KEY (rest_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

CREATE SEQUENCE global_seq_meals START WITH 1000;

CREATE TABLE meals
(   id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq_meals'),
    menu_id          INTEGER                 NOT NULL,
    name             VARCHAR                 NOT NULL,
    price          	 NUMERIC(5,2)            NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menus (id) ON DELETE CASCADE
);

CREATE TABLE votes
(
    user_id          INTEGER                 NOT NULL,
    menu_id          INTEGER                 NOT NULL,
    date_menu        date                    NOT NULL,
    date_reg         TIMESTAMP               NOT NULL,
    CONSTRAINT user_lunch_idx UNIQUE (user_id, date_menu),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (menu_id) REFERENCES menus (id) ON DELETE CASCADE
);