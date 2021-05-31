-- DROP TABLE pi4dhl;
--
-- CREATE SEQUENCE SEQ_NAME AS INTEGER START WITH 100;

CREATE TABLE pi4dhl
(
    id               INTEGER NOT NULL DEFAULT nextval('SEQ_NAME') ,
    date             TIMESTAMP DEFAULT now() NOT NULL,
    temperature      FLOAT                   NOT NULL,
    humidity         FLOAT                   NOT NULL
);