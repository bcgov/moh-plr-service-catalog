-- Delete and recreate PLR_HS_CATALOG database. Run from postgres account.

-- Delete PLR_HS_CATALOG if already been created
drop database IF EXISTS PLR_HS_CATALOG;

CREATE DATABASE plr_hs_catalog
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'English_United States.1252'
    LC_CTYPE = 'English_United States.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
