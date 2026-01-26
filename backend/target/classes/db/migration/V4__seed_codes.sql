-- delete/insert code table records for PLR_HS_CATALOG schema

-- HS_SYSTEM_REF
DELETE
FROM PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN;

INSERT INTO 
	PLR_HS_CATALOG.HS_SYSTEM_OF_ORIGIN (SYSTEM_ID,SYSTEM_CD,SYSTEM_DESC_TXT,EFFECTIVE_START_DT,EFFECTIVE_END_DT)
VALUES  
	(NEXTVAL('PLR_HS_CATALOG.HS_SYSTEM_SEQ'),'PHSA','PHSA - pathways','2023-01-01','9999-01-01'),
	(NEXTVAL('PLR_HS_CATALOG.HS_SYSTEM_SEQ'),'PLR','PLR - MOH','2023-01-01','9999-01-01'),
	(NEXTVAL('PLR_HS_CATALOG.HS_SYSTEM_SEQ'),'MSP','MSP eClaims','2023-01-01','9999-01-01');

INSERT INTO 
	PLR_HS_CATALOG.CODE_SYSTEM (CODE_SYSTEM_ID, CODE_SYSTEM_DESC_TXT, CODE_SYSTEM_LOOKUP_URL, EFFECTIVE_START_DT,EFFECTIVE_END_DT)
VALUES  
	(NEXTVAL('PLR_HS_CATALOG.CODE_SYSTEM_SEQ'),'PLR Expertise CodeSystem', 'http://terminologydev.hlth.gov.bc.ca/ProviderLocationRegistry/CodeSystem/bc-expertise-code-system/','2023-01-01','9999-01-01'),
	(NEXTVAL('PLR_HS_CATALOG.CODE_SYSTEM_SEQ'),'FHIR Standard Specialty CodeSystem', 'http://terminology.hl7.org/CodeSystem/service-type/','2023-01-01','9999-01-01'),
	(NEXTVAL('PLR_HS_CATALOG.CODE_SYSTEM_SEQ'),'FHIR Standard HealthService Type CodeSystem', 'http://terminology.hl7.org/CodeSystem/service-type/', '2023-01-01','9999-01-01'),
	(NEXTVAL('PLR_HS_CATALOG.CODE_SYSTEM_SEQ'),'BC HealthService Type CodeSystem','http://terminology.hlth.gov.bc.ca/PHSA/CodeSystem/bc-expertise-code-system/', '2023-01-01','9999-01-01'); --Random URL to test

-- SYSTEM_PARAMETERS
DELETE
FROM PLR_HS_CATALOG.SYSTEM_PARAMETER;

INSERT INTO 
	PLR_HS_CATALOG.SYSTEM_PARAMETER (SYSTEM_PARAMETER_ID,PARAMETER_CD,PARAMETER_VALUE)
VALUES  
	(NEXTVAL('PLR_HS_CATALOG.SYSTEM_PARAMETER_SEQ'),'specialty.codesystem','not in used or terminology url'),
	(NEXTVAL('PLR_HS_CATALOG.SYSTEM_PARAMETER_SEQ'),'type.codesystem','not in used');


