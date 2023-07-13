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
public interface ServiceCatalogConstant {
    
    public static final String URL = "http://terminology.hlth.gov.bc.ca/ProviderLocationRegistry/";
    
    public static final CodeableConcept JURISDICTION_CAN = new CodeableConcept(new Coding("urn:iso:std:iso:3166", "CA", "Canada"));
    
    // BCCatalogService profile URL
    public static final String BC_CATALOG_SERVICE_PROFILE_URL = "http://hlth.gov.bc.ca/fhir/provider/StructureDefinition/bc-catalogue-service";
    
    public static final String OFFEREDIN_R5_URL = "http://hl7.org/fhir/5.0/StructureDefinition/extension-HealthcareService.offeredIn";
     
}
