-- create PLR Services Catalogue schema with proxy user and role in each db env.

-- Delete PLR_HS_CATALOG schema if it has already been created
drop schema IF EXISTS plr_hs_catalog cascade;

create schema plr_hs_catalog AUTHORIZATION postgres;

-- add proxy USER and ROLE

-- Delete SERVICE_CATALOG_PROXY user if it has already been created
drop user IF EXISTS SERVICE_CATALOG_PROXY;
-- Create the proxy user for Service Catalogue
create user SERVICE_CATALOG_PROXY PASSWORD 'password123'; -- to be setup by DBA
-- Grant access to connect to the DB
grant CONNECT on DATABASE plr_hs_catalog to SERVICE_CATALOG_PROXY;

-- Delete SERVICE_CATALOG_PROXY_ROLE role if it has already been created
drop role IF EXISTS SERVICE_CATALOG_PROXY_ROLE;
-- Create the proxy role for SERVICE_CATALOG_PROXY
create role SERVICE_CATALOG_PROXY_ROLE;
-- Grant USAGE and SELECT access to all tables to the role
grant USAGE on schema plr_hs_catalog to SERVICE_CATALOG_PROXY_ROLE;
grant SELECT on ALL TABLES IN SCHEMA plr_hs_catalog to SERVICE_CATALOG_PROXY_ROLE; 

-- Grant the ROLE to the proxy user
grant SERVICE_CATALOG_PROXY_ROLE to SERVICE_CATALOG_PROXY;


