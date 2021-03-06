DROP DATABASE IF EXISTS mediscreen_test;

CREATE DATABASE mediscreen_test;

USE mediscreen_test;

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

INSERT INTO patient(first_name, last_name, birth_date, gender,address,phone)
VALUES ('Christine','Deschamps','1974-05-25', 'F','1509 Culver St','841-874-6512'),
       ('Gérard','Duhammel','2003-04-27', 'M','834 Binoc Ave"','841-874-6515'),
       ('Wiliam','Martin','2005-01-02','F','748 Townings Dr','841-874-6510'),
       ('Stephan','Martin','2000-01-19','F','748 Townings Dr','841-874-6510'),
       ('Lili','Martin','1950-01-15','F','748 Townings Dr','841-874-6510');

commit;