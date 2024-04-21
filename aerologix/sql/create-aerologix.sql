/* DELETE 'aerologixdb' database*/
DROP SCHEMA IF EXISTS aerologixdb;
/* DELETE USER 'spq' AT LOCAL SERVER*/
DROP USER IF EXISTS 'spq'@'localhost';

/* CREATE 'aerologixDB' DATABASE */
CREATE SCHEMA aerologixdb;
/* CREATE THE USER 'spq' AT LOCAL SERVER WITH PASSWORD 'spq' */
CREATE USER IF NOT EXISTS 'spq'@'localhost' IDENTIFIED BY 'spq';

GRANT ALL ON aerologixdb.* TO 'spq'@'localhost';
