package hlth.gov.bc.ca.serviceCatalog.provider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import hlth.gov.bc.ca.serviceCatalog.entity.ServiceTypeRelationship;
import hlth.gov.bc.ca.serviceCatalog.entity.SpecialtyRelationship;
import hlth.gov.bc.ca.serviceCatalog.repository.ServiceCatalogRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4bc1.model.BCCatalogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author camille.estival
 */
@Component
public class ServiceCatalogProvider implements IResourceProvider{
    
    private static final Logger log = LoggerFactory.getLogger(ServiceCatalogProvider.class);
    
    @Autowired
    ServiceCatalogRepository serviceCatalogRepo;
    
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return BCCatalogService.class;
    }
    
    /**
     * Generate the GET HTTP method to return a single BCCatalogService by its LogicalId
     * @param theId
     * @return a single BCCatalogService
     */
    @Read()
    public BCCatalogService read(@IdParam IdType theId) {
        return getServiceById(theId.getIdPart());
    }

    /**
     * Generate the search (GET) HTTP method to search for BCCatalogService
     * @param name, descritpion of the service, search with wildCard (trailing and leading) and IgnoreCase
     * @param identifier - External identifier of a service, should be unique within a system
     * @param serviceTypeCode, lookupCode from a codeSystem for serviceType
     * @param specialtyCode, lookupCode from a codeSystem for Specialty
     * @param offeredIn, parent of the service, will search for service underneath this service
     * @param systemCode, system code, representing the origin of a services (MSP, PHSA ..)
     * @return list of BCCatalogService
     */
    @Search
    public List<BCCatalogService> search(
            @OptionalParam(name = HealthcareService.SP_NAME) StringParam name,
            @OptionalParam(name = HealthcareService.SP_IDENTIFIER) StringParam identifier, 
            @OptionalParam(name = HealthcareService.SP_SERVICE_TYPE) StringParam serviceTypeCode, 
            @OptionalParam(name = HealthcareService.SP_SPECIALTY) StringParam specialtyCode, 
            @OptionalParam(name = "offered-in") StringParam offeredIn, 
            @OptionalParam(name = "system") StringParam systemCode) {
           
        return this.search(ifNullParam(name),
                ifNullParam(identifier),
                ifNullParam(serviceTypeCode),
                ifNullParam(specialtyCode),
                ifNullParam(offeredIn),
                ifNullParam(systemCode));
    }

    /**
     * query DB to get service by LogicalId
     * @param id, logical ID to search for
     * @return BCCatalogService
     * @throws ResourceNotFoundException 
     */
    private BCCatalogService getServiceById(String id) throws ResourceNotFoundException {
        ServiceCatalog service = serviceCatalogRepo.findByLogicalId(Long.valueOf(id));
        if (service == null) {
            throw new ResourceNotFoundException("Code System not found: "+id);
        }
        log.debug (service.toString());
        return transformToHealthcareService(service);
    }
    
     /**
     * 
     * @param name
     * @param extIdentifier
     * @param serviceTypeCode
     * @param specialtyCode
     * @param offeredIn
     * @param systemCode
     * @return list of BCCatalogService
     */
    private List<BCCatalogService> search(String name, String extIdentifier, String serviceTypeCode, String specialtyCode, String offeredIn, String systemCode){
        
        List <ServiceCatalog> listService = serviceCatalogRepo.searchServiceCatalog(name, extIdentifier, systemCode /**, NumberUtils.createLong(offeredIn)*/);
        log.debug ("how many service found by criteria (name, extIdentifier, system): "+listService.size());
        
        if (offeredIn != null){
            // Loop through the service looking for matches
            listService = listService.stream()
                .filter(next -> parentMatched(next.getParentService(), offeredIn) )
                .collect(Collectors.toList());
            
            log.debug ("how many service found by parent: "+listService.size());
        }
        
        if (serviceTypeCode != null){
            listService = listService.stream()
                .filter(next -> anyTypeMatched(next, serviceTypeCode)  )
                .collect(Collectors.toList());
            
            log.debug ("how many service found by serviceType: "+listService.size());
        }
        
        if (specialtyCode != null){
            listService = listService.stream()
                .filter(next -> anySpecialtyMatched(next, specialtyCode)  )
                .collect(Collectors.toList());
            
            log.debug ("how many service found by specialtyCode: "+listService.size());
        }
            
        return transformList(listService);
    }
    
    /**
     * transform the entity ServiceCatalog to a FHIR profile BCCatalogService 
     * @param service entity ServiceCatalog
     * @return BCCatalogService
     */
    private BCCatalogService transformToHealthcareService(ServiceCatalog service)  {

        BCCatalogService hs = new BCCatalogService();
        hs.setId(Long.toString(service.getLogicalId()));
        hs.setActive(true);
        hs.setName(service.getName());
        hs.setComment(service.getDescription());
        
        hs.addCategory(new CodeableConcept(new Coding("https://terminology.hlth.gov.bc.ca/ProviderLocationRegistry/CodeSystem/bc-service-type-code-system", "catalogue","")));
        
        // currently, only one identifier, with or without a value. If need to add extra, we can create a separate table to store the additional external identifers
        buildIdentifiersList(service, hs);
        buildSpecialtyList(service.getSpecialtyRelationships(), hs);
        buildTypeList(service.getServiceTypeRelationships(), hs);

        if (service.getParentService()!= null){
            BCCatalogService parent = transformToHealthcareService(service.getParentService());
            hs.setOfferedInExtension(new Reference(parent));
        }

        return hs;
    }

    /**
     * Build identifiers for the FHIR profile BCCatalogService
     * @param service entity ServiceCatalog
     * @param fhirService BCCatalogService
     */
    private void buildIdentifiersList(ServiceCatalog service, BCCatalogService fhirService) {
        List <Identifier> listIdentifier = new ArrayList();
        Identifier externalId  = new Identifier();
        externalId.setId(service.getExternalIdentifier());
        externalId.setSystem(service.getSystem().getCode());
        listIdentifier.add(externalId);
        fhirService.setIdentifier(listIdentifier);
    }

    /**
     * build list of Specialty for the FHIR profile BCCatalogService
     * @param specialtyRelList list of entity SpecialtyRelationship
     * @param fhirService BCCatalogService
     */
    private void buildSpecialtyList(Set <SpecialtyRelationship> specialtyRelList, BCCatalogService fhirService) {
        List <CodeableConcept> specialtyList= new ArrayList();
        CodeableConcept specialtyCode;
        Coding code;
        if(!specialtyRelList.isEmpty()){
            for (SpecialtyRelationship specialtyRel : specialtyRelList) {
                code = new Coding(specialtyRel.getCodeSystem().getSystemUrl(), specialtyRel.getLookupCode(), "TODO call to get display value");
                specialtyCode= new CodeableConcept(code);
                specialtyList.add(specialtyCode);
            }
        }
        fhirService.setSpecialty(specialtyList);
    }
    
    /**
     * build list of ServiceType for the FHIR profile BCCatalogService
     * @param typeRelList list of entity ServiceTypeRelationship
     * @param fhirService BCCatalogService
     */
    private void buildTypeList(Set <ServiceTypeRelationship> typeRelList, BCCatalogService fhirService) {
        List <CodeableConcept> typeList= new ArrayList();
        CodeableConcept typeCode;
        Coding code;
        if(!typeRelList.isEmpty()){
            for (ServiceTypeRelationship typeRel : typeRelList) {
                code = new Coding(typeRel.getCodeSystem().getSystemUrl(), typeRel.getLookupCode(), "TODO call to get display value");
                typeCode = new CodeableConcept(code);
                typeList.add(typeCode);
            }
        }
        fhirService.setType(typeList);
    }

    /**
     * 
     * @param listService
     * @return 
     */
    private List<BCCatalogService> transformList(List<ServiceCatalog> listService) {
        List <BCCatalogService> healhcareServiceList = new ArrayList<>();
        BCCatalogService fhirService;
        for (ServiceCatalog service : listService) {
            fhirService = transformToHealthcareService(service);
            healhcareServiceList.add (fhirService);
        }
        return healhcareServiceList;
    }
        
    private static boolean parentMatched(ServiceCatalog parent, String offeredIn) {
        if(parent != null){
            return searchBy(offeredIn, parent.getLogicalId().toString());
        } 
        return false;
    }

    private boolean anyTypeMatched(ServiceCatalog serviceCatalog, String serviceTypeCode) {
        return serviceCatalog.getServiceTypeRelationships()
                .stream()
                .anyMatch(next -> searchBy(serviceTypeCode, next.getLookupCode()));
    }

    private boolean anySpecialtyMatched(ServiceCatalog serviceCatalog, String code) {
        return serviceCatalog.getSpecialtyRelationships()
                .stream()
                .anyMatch(next -> searchBy(code, next.getLookupCode()));
    }

    // TODO this 2 should be Utils method
    private static boolean searchBy(String param, String csValue) {
        return (param == null) || StringUtils.lowerCase(param).equals(StringUtils.lowerCase(csValue));
    }
      
    private String ifNullParam(StringParam value) {
        return (value != null) ? value.getValue() : null;
    }
}