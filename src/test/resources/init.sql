
\c postgres
DROP DATABASE IF EXISTS usersdb WITH (FORCE);
CREATE DATABASE usersdb WITH OWNER = admin ENCODING = 'UTF8';
\c usersdb

drop table IF EXISTS locations_stored_seq;
CREATE SEQUENCE locations_stored_seq;

drop table IF EXISTS locations_stored;
CREATE TABLE locations_stored(
    id INTEGER NOT NULL DEFAULT nextval('locations_stored_seq'),
    country_name VARCHAR NOT NULL,
    state_name VARCHAR,
    cities jsonb not null,
    UNIQUE(country_name, state_name),
    CONSTRAINT locations_stored_idx PRIMARY KEY(id)
);
CREATE INDEX country_name_id ON locations_stored(country_name);
CREATE INDEX state_name_id ON locations_stored(state_name);


drop table IF EXISTS names_stored_seq;
CREATE SEQUENCE names_stored_seq;

drop table IF EXISTS names_stored;
CREATE TABLE names_stored(
    id INTEGER NOT NULL DEFAULT nextval('names_stored_seq'),
    name_user VARCHAR NOT NULL,
    gender_user VARCHAR NOT NULL,
    UNIQUE(name_user, gender_user),
    CONSTRAINT names_stored_idx PRIMARY KEY(id)
);
CREATE INDEX gender_user_id ON names_stored(gender_user);


drop table IF EXISTS last_names_stores_seq;
CREATE SEQUENCE last_names_stores_seq;

drop table IF EXISTS last_names_stores;
CREATE TABLE last_names_stores(
    id INTEGER NOT NULL DEFAULT nextval('last_names_stores_seq'),
    last_name_user VARCHAR NOT NULL,
    UNIQUE(last_name_user),
    CONSTRAINT last_names_stores_idx PRIMARY KEY(id)
);
