CREATE TABLE accounts
(
    ID         serial  NOT NULL PRIMARY KEY,
    FIRST_NAME varchar NOT NULL,
    LAST_NAME  varchar,
    BALANCE    numeric DEFAULT 0
);