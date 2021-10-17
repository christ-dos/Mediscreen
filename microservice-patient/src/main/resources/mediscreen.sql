DROP DATABASE IF EXISTS mediscreen;

CREATE DATABASE mediscreen;

USE mediscreen;

CREATE TABLE patient
(
    id           int auto_increment
        primary key,
    first_name   varchar(100)    not null,
    last_name    varchar(100)    not null,
    birth_date varchar(15)     not null,
    gender       enum ('F', 'M') not null,
    address      varchar(300)    null,
    phone        varchar(20)     null
) ENGINE = innoDB;

INSERT INTO patient(first_name, last_name, birth_date, gender)
VALUES ('Christine','Duarte','1974-09-17', 'F');

commit;