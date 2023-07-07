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
	'including Ultrasounds, XRAY and MRI',
	'2023-01-01', 
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE  VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'XRAY', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PHSA'), 
	'X-Ray', 
	'X-Ray services',
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'),  
	'XRAY services from PHSA', 
	'including Ultrasounds', 
	'2023-01-01', 
	'9999-01-01');

INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE  VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'ULTRAS', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PHSA'), 
	'Ultrasounds', 
	'Ultrasounds services',
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'X-Ray'),  
	'Ultrasounds services from PHSA', 
	'', 
	'2023-01-01', 
	'9999-01-01');

INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'MRI', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PHSA'), 
	'MRI', 
	'Magnetic Resonance Imaging (MRI)',
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'),  
	'MRI services from PHSA', 
	'notes', 
	'2023-01-01',
	'9999-01-01'); 

-- TODO add more services, for PLR, and one with same Ext_id
	
-- TODO add link to TYPE and Specialty - SPECIALTY_REL and SERVICE_TYPE_REL
Delete from PLR_HS_CATALOG.service_type_rel where catalog_service_id= (SELECT catalog_service_id FROM PLR_HS_CATALOG.CATALOG_SERVICE serv, PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN sys WHERE SYSTEM_CD = 'PHSA' and sys.system_id= serv.SYSTEM_ID); 

INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'209', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'FHIR_HS_TYPE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'X-Ray'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'209', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'FHIR_HS_TYPE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'), 
	'2023-01-01',
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'210', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'FHIR_HS_TYPE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Ultrasounds'), 
	'2023-01-01',
	'9999-01-01');

INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'211', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'FHIR_HS_TYPE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'MRI'), 
	'2023-01-01',
	'9999-01-01');
	
	
	
Delete from PLR_HS_CATALOG.SPECIALTY_REL where catalog_service_id = (SELECT catalog_service_id FROM PLR_HS_CATALOG.CATALOG_SERVICE serv, PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN sys WHERE SYSTEM_CD = 'PHSA' and sys.system_id= serv.SYSTEM_ID); 

INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'117', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PLR_EXPERTISE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'580', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PLR_EXPERTISE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'), 
	'2023-01-01',
	'9999-01-01');

INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'ORD', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PLR_EXPERTISE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'), 
	'2023-01-01',
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'117', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PLR_EXPERTISE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'X-Ray'), 
	'2023-01-01',
	'9999-01-01'); 
	
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD45', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PLR_EXPERTISE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Ultrasounds'), 
	'2023-01-01',
	'9999-01-01'); 
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD29', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PLR_EXPERTISE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Ultrasounds'), 
	'2023-01-01',
	'9999-01-01'); 
	
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD16', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PLR_EXPERTISE'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'MRI'), 
	'2023-01-01',
	'9999-01-01'); 