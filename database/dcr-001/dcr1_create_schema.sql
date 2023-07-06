------------------------------------------------------------------------------
-- PLR Service Catalogue: 
-- Create PLR_HS_CATALOG schema.
-- Create SERVICE_CATALOG_PROXY user.
-- Create SERVICE_CATALOG_PROXY role.
-- Create PLR_HS_CATALOG tables
-- Populate code tables
-- Load test data
--
-- Created by: Camille Estival (camille.estival@cgi.com)
-- Created on : June 12, 2023
------------------------------------------------------------------------------

-- Run with postgres (any admin) account on PLR_HS_CATALOG Database (need to create DB first)

-- write output to the log file
-- spool log.txt;

-- Create PLR_HS_CATALOG schema and its SERVICE_CATALOG_PROXY user.
--PROMPT ********* Create PLR_HS_CATALOG schema and its SERVICE_CATALOG_PROXY user.
@01_create_schema_service_catalogue.sql

-- Create tables in PLR_HS_CATALOG schema
--PROMPT ********* Create tables in PLR_HS_CATALOG schema.
@02_create_service_catalogue_ddl.sql

-- Populate codes tables
--PROMPT ********* Populate codes tables.
@03_service_catalogue_dml.sql

-- Load tables with test data
--PROMPT ********* Load tables with test data.
@04_service_catalogue_test_data_dml.sql

-- close the output file
-- spool OFF;

-- Exit SQL*Plus.
--PROMPT ***** Disconnecting *****
exit

------------------------------------------------------------------------------
-- End of the script
------------------------------------------------------------------------------