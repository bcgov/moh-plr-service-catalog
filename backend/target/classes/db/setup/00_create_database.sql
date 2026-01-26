-- This script should be run by a PostgreSQL superuser (e.g., postgres)

-- Create the user for the application
CREATE USER plr_user WITH PASSWORD 'plr_password';

-- Create the database
CREATE DATABASE plr_hs_catalog
    WITH
    OWNER = plr_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Grant all privileges on the new database to the new user
GRANT ALL PRIVILEGES ON DATABASE plr_hs_catalog TO plr_user;
