CREATE TABLE transfers
(
    ID            serial    NOT NULL PRIMARY KEY,
    SOURCE_ID     int       NOT NULL,
    TARGET_ID     int       NOT NULL,
    AMOUNT        int       NOT NULL DEFAULT 0,
    TRANSFER_TIME timestamp NOT NULL DEFAULT now(),
    FOREIGN KEY (SOURCE_ID) REFERENCES accounts (id),
    FOREIGN KEY (TARGET_ID) REFERENCES accounts (id)
);