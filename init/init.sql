DROP TABLE IF EXISTS shift;
DROP TABLE IF EXISTS driver;
DROP TABLE IF EXISTS boundary;
CREATE TABLE driver (
    driver_id serial PRIMARY KEY,
    driver_name varchar,
    driver_surname varchar,
    driver_birthday timestamptz,
    driver_address text,
    driver_position geometry
);
CREATE TABLE shift(
    shift_id serial PRIMARY KEY,
    driver_id int references driver(driver_id) ON DELETE CASCADE,
    shift_start timestamptz,
    shift_end timestamptz
);
CREATE TABLE boundary(
    boundary_id serial PRIMARY KEY,
    boundary_name varchar,
    boundary_geometry geometry
);
INSERT INTO driver(driver_name, driver_surname, driver_birthday, driver_address, driver_position)
VALUES ('Harrier', 'Du Bois', TIMESTAMPTZ '19700505', '---', 'SRID=4326;POINT(34.77467769212977 32.079017923504175)'::geometry),
       ('Raphael Ambrosius', 'Costeau', TIMESTAMPTZ '19751012', '---', 'SRID=4326;POINT(35.21630705717929 31.780081269924974)'::geometry),
       ('Tequila', 'Sunset', TIMESTAMPTZ '19780802', '---', 'SRID=4326;POINT(35.00269378257618 32.81609071530166)'::geometry);
INSERT INTO shift(driver_id, shift_start, shift_end)
VALUES (1, TIMESTAMPTZ '2022-10-31 04:00:00', TIMESTAMPTZ '2022-10-31 12:00:00'),
       (2, TIMESTAMPTZ '2022-10-31 12:00:00', TIMESTAMPTZ '2022-10-31 20:00:00');

