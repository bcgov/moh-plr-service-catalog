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
import hlth.gov.bc.ca.serviceCatalog.entity.SystemOfOrigin;
import hlth.gov.bc.ca.serviceCatalog.repository.ServiceCatalogRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import org.springframework.data.domain.Example;
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
    
    //A Map to hold all the codeSystems with their codes. Expiry time is set to 1 hour.
//    private final PassiveExpiringMap<String, CodeSystem> codeSystemMap = new PassiveExpiringMap<>(3600000);
    
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return BCCatalogService.class;
    }
    
    @Read()
    public BCCatalogService read(@IdParam IdType theId) {
        return getServiceById(theId.getIdPart());
    }

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


    private BCCatalogService getServiceById(String id) throws ResourceNotFoundException {
        ServiceCatalog service = serviceCatalogRepo.findByLogicalId(Long.valueOf(id));
        if (service == null) {
            throw new ResourceNotFoundException("Code System not found: "+id);
        }
        log.debug (service.toString());
        return transformToHealthcareService(service);
    }

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

    private void buildIdentifiersList(ServiceCatalog service, BCCatalogService hs) {
        List <Identifier> listIdentifier = new ArrayList();
        Identifier externalId  = new Identifier();
        externalId.setId(service.getExternalIdentifier());
        externalId.setSystem(service.getSystem().getCode());
        listIdentifier.add(externalId);
        hs.setIdentifier(listIdentifier);
    }

    private void buildSpecialtyList(Set <SpecialtyRelationship> specialtyRelList, BCCatalogService hs) {
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
        hs.setSpecialty(specialtyList);
    }
    
    private void buildTypeList(Set <ServiceTypeRelationship> typeRelList, BCCatalogService hs) {
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
        hs.setType(typeList);
    }
        
    private List<BCCatalogService> search(String name, String extIdentifier, String serviceTypeCode, String specialtyCode, String offeredIn, String systemCode){
        
//        ServiceCatalog criteria = ServiceCatalog.builder()
//                .name(name)
//                .externalIdentifier(extIdentifier)
//                .system(SystemOfOrigin.builder().code(systemCode).build())
//                .parentService(ServiceCatalog.builder().logicalId(NumberUtils.createLong(offeredIn)).build())
//                .specialtyRelationship(SpecialtyRelationship.builder().lookupCode(specialtyCode).build())
//                .serviceTypeRelationship(ServiceTypeRelationship.builder().lookupCode(serviceTypeCode).build())
//                .build();
        
//        List <ServiceCatalog> listService = serviceCatalogRepo.findAll(Example.of(criteria));
//        log.debug ("how many service found by criteria (name, extIdentifier or system): "+listService.size());
        
        
        List <ServiceCatalog> listService = serviceCatalogRepo.searchServiceCatalog(name, extIdentifier, systemCode, NumberUtils.createLong(offeredIn));
        log.debug ("how many service found by criteria (name, extIdentifier, system, parent): "+listService.size());
        
//        if (offeredIn != null){
//            // Loop through the service looking for matches
//            listService = listService.stream()
//                .filter(next -> parentMatched(next.getParentService(), offeredIn) )
//                .collect(Collectors.toList());
//            
//            log.debug ("how many service found by parent: "+listService.size());
//        }
        
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
            

            // if need recursive search by parent (aka drill down to see next levels children), add a different parameter to search request
//            listService = serviceCatalogRepo.findByParent(new Long(offeredIn));
//            log.debug ("how many service found by parent: "+listService.size());
            
//            // Should be unique (extIdentifier + system)
//            listService = serviceCatalogRepo.findByExternalIdentifierAndSystem(extIdentifier, systemCode);
//            log.debug ("how many service found by extIdentifier+system: "+listService.size());

//            List<ServiceCatalog> rawServiceList = serviceCatalogRepo.findBySystemCode(systemCode);
//            log.debug ("how many service found by system: "+rawServiceList.size());
//            // Loop through the service looking for matches
//            listService = rawServiceList.stream()
//                .filter(next -> searchBy(name, next.getName()) )
//                .collect(Collectors.toList());

        return transformList(listService);
    }

    public static boolean parentMatched(ServiceCatalog parent, String offeredIn) {
        if(parent != null){
            return searchBy(offeredIn, parent.getLogicalId().toString());
        } 
        return false;
    }

    public boolean anyTypeMatched(ServiceCatalog serviceCatalog, String serviceTypeCode) {
        return serviceCatalog.getServiceTypeRelationships()
                .stream()
                .anyMatch(next -> searchBy(serviceTypeCode, next.getLookupCode()));
    }

    public boolean anySpecialtyMatched(ServiceCatalog serviceCatalog, String code) {
        return serviceCatalog.getSpecialtyRelationships()
                .stream()
                .anyMatch(next -> searchBy(code, next.getLookupCode()));
    }
    
    private List<BCCatalogService> transformList(List<ServiceCatalog> listService) {
        List <BCCatalogService> healhcareServiceList = new ArrayList<>();
        BCCatalogService hs;
        for (ServiceCatalog service : listService) {
            hs = transformToHealthcareService(service);
            healhcareServiceList.add (hs);
        }
        return healhcareServiceList;
    }


    // TODO this 2 should be Utils method
    private static boolean searchBy(String param, String csValue) {
        return (param == null) || StringUtils.lowerCase(param).equals(StringUtils.lowerCase(csValue));
    }
      
    private String ifNullParam(StringParam value) {
        return (value != null) ? value.getValue() : null;
    }
}