-- first Delete all from PHSA origin

Delete from PLR_HS_CATALOG.CATALOG_SERVICE where SYSTEM_ID= (SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PHSA'); 

-- Add few services in a hierarchy from PHSA origin: Imaging>XRAY>MRI ...
INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'IMAG', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PHSA'), 
	'Imaging', 
	'Imaging services', 
	null, 
	'all the Imaging services from PHSA', 
	'including Ultrasounds and XRAY',
	'2023-01-01', 
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE  VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'XRAY', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PHSA'), 
	'X-Ray', 
	'X-Ray services',
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'IMAG'),  
	'XRAY services from PHSA', 
	'including MRI', 
	'2023-01-01', 
	'9999-01-01');

INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'MRI', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PHSA'), 
	'Medical Radio Imaging', 
	'MRI service',
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'XRAY'),  
	'MRI services from PHSA', 
	'notes', 
	'2023-01-01',
	'9999-01-01'); 

-- TODO add more services, for PLR, and one with same Ext_id


-- TODO add link to TYPE and Specialty - SPECIALTY_REL and SERVICE_TYPE_REL
