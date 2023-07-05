package hlth.gov.bc.ca.serviceCatalog.provider;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import hlth.gov.bc.ca.serviceCatalog.repository.ServiceCatalogRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author camille.estival
 */
@Component
public class ServiceCatalogProvider implements IResourceProvider{
    
    private static final Logger log = LoggerFactory.getLogger(ServiceCatalogProvider.class);
    
//    @Autowired
//    JdbcTemplate jdbcTemplate;
    
    @Autowired
    ServiceCatalogRepository serviceCatalogRepo;
    
    //A Map to hold all the codeSystems with their codes. Expiry time is set to 1 hour.
    private final PassiveExpiringMap<String, CodeSystem> codeSystemMap = new PassiveExpiringMap<>(3600000);
    
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return HealthcareService.class;
    }
    

    
    @Read()
    public HealthcareService read(@IdParam IdType theId) {
        return getServiceById(theId.getIdPart());
    }

    // TODO need to add search by service Type, specialty, ExternalIdentifier, System and Parent
    @Search
    public List<HealthcareService> search(
            @OptionalParam(name = HealthcareService.SP_NAME) StringParam name,
            @OptionalParam(name = HealthcareService.SP_IDENTIFIER) StringParam identifier ) {
        
        return this.search(ifNullParam(name));
    }


    private HealthcareService getServiceById(String id) throws ResourceNotFoundException {
        ServiceCatalog service = serviceCatalogRepo.findByLogicalId(new Long(id));
        if (service == null) {
            throw new ResourceNotFoundException("Code System not found: "+id);
        }
        log.debug (service.toString());
        return transformToHealthcareService(service);
    }

    private HealthcareService transformToHealthcareService(ServiceCatalog service)  {

        HealthcareService hs = new HealthcareService();
        hs.setId(Long.toString(service.getLogicalId()));
        hs.setActive(true);
        hs.setName(service.getName());
        hs.setComment(service.getDescription());
        
        hs.addCategory(new CodeableConcept(new Coding("https://terminology.hlth.gov.bc.ca/ProviderLocationRegistry/CodeSystem/bc-service-type-code-system", "catalogue","")));
        
        List <Identifier> listIdentifier = new ArrayList();
        Identifier logicalId  = new Identifier();
        logicalId.setId(Long.toString(service.getLogicalId()));
        logicalId.setSystem(service.getSystem().getCode());
        listIdentifier.add(logicalId);
        Identifier externalId  = new Identifier();
        externalId.setId(service.getExternalIdentifier());
        externalId.setSystem("external");
        listIdentifier.add(externalId);
        hs.setIdentifier(listIdentifier);
        if(service.getParentService()!=null){
            List <Extension> extensionList = new ArrayList<>();
            Extension offeredIn = new Extension("http://hl7.org/fhir/5.0/StructureDefinition/extension-HealthcareService.offeredIn");
            offeredIn.setId("offeredIn");
//            offeredIn.setValue(getHealthcareService(service.getParentService()));
// TODO or should it be a Reference?
            offeredIn.setUserData("offeredIn", transformToHealthcareService(service.getParentService()));
            extensionList.add(offeredIn);
            hs.setExtension(extensionList);
        }

        return hs;
    }

    
    private List<HealthcareService> search(String name){
        
        List <ServiceCatalog> listService = serviceCatalogRepo.findByName(name);
        
        log.debug ("how many service found by name:"+listService.size());
        
        return transformList(listService);
        
    }

    public List<HealthcareService> transformList(List<ServiceCatalog> listService) {
        List <HealthcareService> healhcareServiceList = new ArrayList<>();
        HealthcareService hs;
        for (ServiceCatalog service : listService) {
            hs = transformToHealthcareService(service);
            healhcareServiceList.add (hs);
        }
        return healhcareServiceList;
        
        // Loop through the patients looking for matches
//         List<CodeSystem> codesList = codeSystemMap.values()
//                .stream()
//                .filter(next -> searchBy(url, next.getUrl()) )
//                .filter(next -> searchBy(name, next.getName()) )
//                .filter(next -> searchBy(title, next.getTitle()) )
//                .collect(Collectors.toList());
//        return codesList;
    }



    // TODO this 2 should be Utils method
    private static boolean searchBy(String param, String csValue) {
        return (param == null) || StringUtils.lowerCase(param).equals(StringUtils.lowerCase(csValue));
    }
      
    private String ifNullParam(StringParam value) {
        return (value != null) ? value.getValue() : null;
    }
}
