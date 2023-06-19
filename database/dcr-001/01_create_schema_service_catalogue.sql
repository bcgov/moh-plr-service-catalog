-- create PLR Services Catalogue schema with proxy user and role in each db env.

-- Delete PLR_HS_CATALOG schema if it has already been created
drop schema IF EXISTS PLR_HS_CATALOG cascade;

CREATE SCHEMA PLR_HS_CATALOG
AUTHORIZATION postgres;

-- add proxy USER

-- Delete SERVICE_CATALOG_PROXY user if it has already been created
drop user IF EXISTS SERVICE_CATALOG_PROXY;

-- Create the proxy user for Service Catalogue
create user SERVICE_CATALOG_PROXY PASSWORD ''; -- to be setup by DBA
-- Grant access to connect to the DB
GRANT CONNECT on DATABASE plr_hs_catalog TO SERVICE_CATALOG_PROXY;


-- Delete SERVICE_CATALOG_PROXY_ROLE role if it has already been created
drop role IF EXISTS  SERVICE_CATALOG_PROXY_ROLE;

-- Create the proxy role for SERVICE_CATALOG_PROXY
create ROLE SERVICE_CATALOG_PROXY_ROLE;

-- Grant SELECT access to all tables to the role
GRANT SELECT on ALL TABLES IN SCHEMA plr_hs_catalog TO SERVICE_CATALOG_PROXY_ROLE; 

-- Grant the ROLE to the proxy user
grant SERVICE_CATALOG_PROXY_ROLE to SERVICE_CATALOG_PROXY;


