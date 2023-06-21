/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hlth.gov.bc.ca.serviceCatalog;

import java.util.HashMap;
import java.util.Map;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

/**
 *
 * @author camille.estival
 */
public final class ServiceCatalogConstant {
    
    
    public static final String URL = "http://terminology.hlth.gov.bc.ca/ProviderLocationRegistry/";
    
    public static final CodeableConcept JURISDICTION_CAN = new CodeableConcept(new Coding("urn:iso:std:iso:3166", "CA", "Canada"));

    public static final Map<String, String> PLR_CODE_SYSTEM_MAP = new HashMap<>();
    static {
        PLR_CODE_SYSTEM_MAP.put("bc-language-code-system", "SELECT * FROM PLR.PRS_CT_PROV_EXPERTISE_TYPES where END_REASON_CODE is null and PROV_ROLE_TYPE = 'LANG' ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-expertise-code-system", "SELECT * FROM PLR.PRS_CT_PROV_EXPERTISE_TYPES where END_REASON_CODE is null and (PROV_ROLE_TYPE != 'LANG' or PROV_ROLE_TYPE is null) ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-credential-code-system", "SELECT * FROM PLR.PRS_CT_PROV_CREDENTIAL_TYPES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-license-status-reason-code-system", "SELECT * FROM PLR.PRS_CT_STATUS_REASON_CODES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-license-status-code-system", "SELECT * FROM PLR.PRS_CT_STATUS_CODES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-license-status-class-code-system", "SELECT * FROM PLR.PRS_CT_STATUS_CLASS_CODES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-condition-type-code-system", "SELECT * FROM PLR.PRS_CT_CONDITION_TYPES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-relationship-type-code-system", "SELECT * FROM PLR.PRS_CT_RELATIONSHIP_TYPES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-end-reason-code-system", "SELECT * FROM PLR.GRS_CT_END_REASON_TYPES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-location-identifier-type-code-system", "SELECT * FROM PLR.PLR_CT_FACILITY_IDENT_TYPE where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-owner-code-system", "SELECT * FROM PLR.PRS_CT_DATA_OWNER_CODES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-communication-purpose-code-system", "SELECT * FROM PLR.PRS_CT_COMMUN_PURPOSE_TYPES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-address-validation-status-code-system", "SELECT * FROM PLR.PRS_CT_ADDRESS_VALID_TYPES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-location-relationship-type-code-system", "SELECT * FROM PLR.PLR_CT_FACILITY_RELN_TYPES where END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-practitioner-role-code-system", "SELECT * FROM PLR.PRS_CT_PROVIDER_ROLE_TYPES where PARTY_TYPE = 'IND' and END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        PLR_CODE_SYSTEM_MAP.put("bc-organization-role-code-system", "SELECT * FROM PLR.PRS_CT_PROVIDER_ROLE_TYPES where PARTY_TYPE = 'ORG' and END_REASON_CODE is null ORDER by EFFECTIVE_START_DATE DESC");
        //queriesMap.put("bc-error-code-system", ""); // Error codes are defined in PLR code at the moment so we cannot exposed them (yet? TBD)
    }
    
    public static final HashMap<String, String> PLR_VALUE_SET_MAPPING = new HashMap<>();
    static {
        // Some of the code systems these value sets contain are not from the PLR database so won't be contained in the ValueSet/$expand
        PLR_VALUE_SET_MAPPING.put("bc-language-value-set", "bc-language-code-system");
        PLR_VALUE_SET_MAPPING.put("bc-qualification-value-set", "bc-credential-code-system, http://fhir.infoway-inforoute.ca/CodeSystem/scpqual");
        PLR_VALUE_SET_MAPPING.put("bc-expertise-value-set", "bc-expertise-code-system, http://fhir.infoway-inforoute.ca/CodeSystem/scpqual");
        PLR_VALUE_SET_MAPPING.put("bc-practitioner-role-value-set", "bc-practitioner-role-code-system, http://fhir.infoway-inforoute.ca/CodeSystem/scptype ");
        PLR_VALUE_SET_MAPPING.put("bc-organization-role-value-set", "bc-organization-role-code-system");
        PLR_VALUE_SET_MAPPING.put("bc-license-status-value-set", "bc-license-status-code-system, http://terminology.hl7.org/CodeSystem/v3-RoleStatus");
        PLR_VALUE_SET_MAPPING.put("bc-license-status-reason-value-set", "bc-license-status-reason-code-system");
        PLR_VALUE_SET_MAPPING.put("bc-license-status-class-value-set", "bc-license-status-class-code-system");
    }

}
