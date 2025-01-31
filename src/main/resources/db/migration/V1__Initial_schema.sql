CREATE TABLE house (
                       id                  BIGSERIAL PRIMARY KEY NOT NULL,
                       code                varchar(255) NOT NULL,
                       name                varchar(255) NOT NULL,
                       city                varchar(255) NOT NULL,
                       state               varchar(255) NOT NULL,
                       photo               varchar(255) NOT NULL,
                       available_units     integer NOT NULL,
                       wifi                BOOL NOT NULL,
                       laundry             BOOL NOT NULL,
                       created_date        timestamp NOT NULL,
                       last_modified_date  timestamp NOT NULL,
                       version             integer NOT NULL
);