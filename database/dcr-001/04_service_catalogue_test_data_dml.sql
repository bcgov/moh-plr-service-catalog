
-- first Delete all from PHSA origin
Delete from PLR_HS_CATALOG.service_type_rel where catalog_service_id in (SELECT catalog_service_id FROM PLR_HS_CATALOG.CATALOG_SERVICE serv, PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN sys WHERE SYSTEM_CD = 'PHSA' and sys.system_id= serv.SYSTEM_ID); 

Delete from PLR_HS_CATALOG.SPECIALTY_REL where catalog_service_id in (SELECT catalog_service_id FROM PLR_HS_CATALOG.CATALOG_SERVICE serv, PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN sys WHERE SYSTEM_CD = 'PHSA' and sys.system_id= serv.SYSTEM_ID); 

Delete from PLR_HS_CATALOG.CATALOG_SERVICE where SYSTEM_ID = (SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'PHSA'); 

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

-- add specialty and service type to PHSA services

INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'209', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'FHIR%Type%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'X-Ray'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'209', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'FHIR%Type%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'), 
	'2023-01-01',
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'210', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'FHIR%Type%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'ULTRAS'), 
	'2023-01-01',
	'9999-01-01');

INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'211', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'FHIR%Type%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'MRI'), 
	'2023-01-01',
	'9999-01-01');
	
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'117', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'580', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'), 
	'2023-01-01',
	'9999-01-01');

INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'ORD', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Imaging'), 
	'2023-01-01',
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'117', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'X-Ray'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD45', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'ULTRAS'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD29', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'ULTRAS'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD16', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'MRI'), 
	'2023-01-01',
	'9999-01-01'); 
	



-- MSP services XRAY, MRI, Ultrasounds>PRenatal+cardio
Delete from PLR_HS_CATALOG.service_type_rel where catalog_service_id in (SELECT catalog_service_id FROM PLR_HS_CATALOG.CATALOG_SERVICE serv, PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN sys WHERE SYSTEM_CD = 'MSP' and sys.system_id= serv.SYSTEM_ID); 

Delete from PLR_HS_CATALOG.SPECIALTY_REL where catalog_service_id in (SELECT catalog_service_id FROM PLR_HS_CATALOG.CATALOG_SERVICE serv, PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN sys WHERE SYSTEM_CD = 'MSP' and sys.system_id= serv.SYSTEM_ID); 

Delete from PLR_HS_CATALOG.CATALOG_SERVICE where SYSTEM_ID = (SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'MSP');

INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE  VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'XR', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'MSP'), 
	'X-Ray billing', 
	'X-Ray performed by physicians',
	null,  
	'XRAY for MSP billing', 
	'not including Ultrasounds', 
	'2023-01-01', 
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE  VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'MRI', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'MSP'), 
	'Magnetic Resonance Imaging', 
	'MRI performed at hospital',
	null,  
	'MRI for MSP billing', 
	'at hospital only', 
	'2023-01-01', 
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE  VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'US', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'MSP'), 
	'Ultrasounds', 
	'Ultrasounds, echo, internal and cardio',
	null,  
	'Ultrasounds for MSP billing', 
	'Hospital and at clinic', 
	'2023-01-01', 
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE  VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'US-P', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'MSP'), 
	'Prenatal Ultrasounds', 
	'Ultrasounds - prenatal',
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'US'), 
	'Ultrasounds for MSP billing', 
	'Hospital and at clinic', 
	'2023-01-01', 
	'9999-01-01');
	
INSERT INTO PLR_HS_CATALOG.CATALOG_SERVICE  VALUES(
	NEXTVAL('PLR_HS_CATALOG.CATALOG_SERVICE_SEQ'), 
	NEXTVAL('PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ'),
	'US-C', 
	(SELECT SYSTEM_ID FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN WHERE SYSTEM_CD = 'MSP'), 
	'Cardio Ultrasounds', 
	'Ultrasounds - cardio',
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'US'), 
	'Ultrasounds for MSP billing', 
	'Hospital and at clinic', 
	'2023-01-01', 
	'9999-01-01');

	
-- add specialty and service type to MSP services

INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'209', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'FHIR%Type%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'XR'), 
	'2023-01-01',
	'9999-01-01'); 

	
INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'210', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'FHIR%Type%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'US'), 
	'2023-01-01',
	'9999-01-01');

INSERT INTO PLR_HS_CATALOG.service_type_rel VALUES(
	NEXTVAL('PLR_HS_CATALOG.SERVICE_TYPE_REL_SEQ'), 
	'211', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'FHIR%Type%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Magnetic Resonance Imaging'), 
	'2023-01-01',
	'9999-01-01');

	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'117', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'XR'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD45', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'US'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD29', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_EXT_IDENT = 'US'), 
	'2023-01-01',
	'9999-01-01'); 
	
INSERT INTO PLR_HS_CATALOG.SPECIALTY_REL VALUES(
	NEXTVAL('PLR_HS_CATALOG.SPECIALTY_REL_SEQ'), 
	'AMD16', 
	(SELECT CODE_SYSTEM_ID FROM PLR_HS_CATALOG.CODE_SYSTEM WHERE CODE_SYSTEM_DESC_TXT LIKE 'PLR%'), 
	(SELECT CATALOG_SERVICE_ID FROM PLR_HS_CATALOG.CATALOG_SERVICE WHERE SERVICE_NAME = 'Magnetic Resonance Imaging'), 
	'2023-01-01',
	'9999-01-01'); 