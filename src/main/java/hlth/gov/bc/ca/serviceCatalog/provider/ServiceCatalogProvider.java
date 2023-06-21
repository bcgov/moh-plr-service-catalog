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
import hlth.gov.bc.ca.serviceCatalog.ServiceCatalogConstant;
import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import hlth.gov.bc.ca.serviceCatalog.repository.ServiceCatalogRepository;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HealthcareService;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author camille.estival
 */
@Component
public class ServiceCatalogProvider implements IResourceProvider {
    
    private static final Logger log = LoggerFactory.getLogger(ServiceCatalogProvider.class);
    
//    @Autowired
//    JdbcTemplate jdbcTemplate;
    
    @Autowired
    ServiceCatalogRepository serviceCatalogRepo;
    
    //A Map to hold all the codeSystems with their codes. Expiry time is set to 1 hour.
    private final PassiveExpiringMap<String, CodeSystem> codeSystemMap = new PassiveExpiringMap<>(3600000);
    
    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return CodeSystem.class;
    }

    
    @Read()
    public HealthcareService read(@IdParam IdType theId) {
        return getServiceById(theId.getIdPart());
    }

//    @Search
//    public List<CodeSystem> search(@OptionalParam(name = CodeSystem.SP_URL) StringParam url,
//            @OptionalParam(name = CodeSystem.SP_NAME) StringParam name,
//            @OptionalParam(name = CodeSystem.SP_TITLE) StringParam title ) {
//        
//        return this.search(ifNullParam(url), 
//                ifNullParam(name),
//                ifNullParam(title));
//    }





   protected HealthcareService getServiceById(String id) throws ResourceNotFoundException {
//        manageCSCache();
        
        ServiceCatalog service = serviceCatalogRepo.getReferenceById(new Long(id));
        if (service == null) {
            throw new ResourceNotFoundException("Code System not found: "+id);
        }
        
        HealthcareService hs = new HealthcareService(); // tODO transform service to HS
        return hs;
    }

//    private CodeSystem getCodeSystemBySystem(String system) throws InternalErrorException, ResourceNotFoundException {
//        
//        if (system != null && !system.isBlank()) {
//            String[] systemSplit = system.split("/");
//            String theId = systemSplit[systemSplit.length - 1];
//            if (!theId.isEmpty()) {
//               return getCodeSystemById(theId);
//            }
//        }
//        throw new InvalidRequestException("Parameter 'system' is mandatory if resource ID is not specified.");
//    }
    
    private List<CodeSystem> search(String url, String name, String title){
        // load cached Map
//        manageCSCache();
        
        // Loop through the patients looking for matches
         List<CodeSystem> codesList = codeSystemMap.values()
                .stream()
                .filter(next -> searchBy(url, next.getUrl()) )
                .filter(next -> searchBy(name, next.getName()) )
                .filter(next -> searchBy(title, next.getTitle()) )
                .collect(Collectors.toList());
        return codesList;
    }
    
    private void isCodeParamEmpty(String code) throws InvalidRequestException {
        if(code == null || code.isBlank()){
            throw new InvalidRequestException("The parameter 'code' is mandatory for the lookup operation");
        }
    }
        
    /** 
     * load a cache with the codeSytem resource
     * Called in search and getBy ID
     */
//    private void manageCSCache() {
//        
//        log.info("start manageCacheCS method");
//        if (codeSystemMap.isEmpty() || codeSystemMap.size()!= ServiceCatalogConstant.PLR_CODE_SYSTEM_MAP.size()){
//            
//            log.info("CS map is empty or not complete, codeSystemMap.size:" +codeSystemMap.size());
//            
//            for (String id : ServiceCatalogConstant.PLR_CODE_SYSTEM_MAP.keySet()) {
//                if (codeSystemMap.get(id) == null) {
//                    try {
//                        codeSystemMap.put(id, loadCodeSystemById(id));
//                        log.info("add CS to map for id : " +id);
//                    } catch (ResourceNotFoundException e){
//                        log.error("query result not found for id : " +id);
//                    } catch (DataAccessException e){
//                        log.error("There is an issue with the DB query with this Code System: " +id);
//                    }
//                }
//            }
//        }
//        log.info("manageCacheCS method exit");
//    }
    
//    private CodeSystem loadCodeSystemById(String theId) throws DataAccessException, ResourceNotFoundException {        
//        CodeSystem cs;
//        
//        List<Map<String, Object>> resultList = getQueryResult(StringUtils.lowerCase(theId));
//        if (resultList == null) {
//            log.debug("query result KO or this ID does not exist");
//            throw new ResourceNotFoundException(theId);
//        } else {
//                cs = new CodeSystem();
//                cs.setId(theId);
//                cs.setUrl(ServiceCatalogConstant.URL + "CodeSystem/" + theId);
//                cs.setName(CaseUtils.toCamelCase(theId.replace("-", " "), true));
//                cs.setTitle(CaseUtils.toCamelCase(theId.replace("-", " "), true));
//                cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
//                cs.setPublisher("Government of British Columbia");
//                cs.setJurisdiction(Collections.singletonList(ServiceCatalogConstant.JURISDICTION_CAN));
//                
//            for (Map m : resultList) {
//                if (cs.getDate() == null){
//                        // Add date to CS based on latest code added/edited in DB (aka first from the list)
//                        cs.setDate((Timestamp) m.get("EFFECTIVE_START_DATE"));
//                }
//                CodeSystem.ConceptDefinitionComponent cdc = new CodeSystem.ConceptDefinitionComponent();
//                cdc.setCode((String) m.get("CTL_NAME_CODE"));
//                cdc.setDisplay((String) m.get("CTL_DESC_TXT"));
//                    
//                if (theId.matches("bc-expertise-code-system") && m.get("PROV_ROLE_TYPE") != null) {
//                        cdc.setDefinition("Only applicable for the following roles: " + ((String) m.get("PROV_ROLE_TYPE")));
//                }
//                if (theId.matches("bc-credential-code-system") && m.get("PROV_ROLE_TYPE") != null) {
//                        cdc.setDefinition("Only applicable for the following roles: " + ((String) m.get("PROV_ROLE_TYPE")));
//                }
//                cs.addConcept(cdc);
//            }
//            this.codeSystemMap.put(theId, cs);
//        }
//        return cs;
//    }
    
    /**
     * Load the query results 
     * @param csId
     * @return
     * @throws InternalErrorException 
     */
//    private List<Map<String, Object>> getQueryResult(String csId) throws InternalErrorException {
//        
//        log.debug("getQueryResult by id :" + csId);
//        if (ServiceCatalogConstant.PLR_CODE_SYSTEM_MAP.get(csId)!= null) {
//            List<Map<String, Object>> list; 
//        
//                try {
//                    list = jdbcTemplate.queryForList(ServiceCatalogConstant.PLR_CODE_SYSTEM_MAP.get(csId));
//                } catch (DataAccessException e){
//                    throw new InternalErrorException("DB query failed", e);
//                }
//                log.debug("query ok :" + list.size());
//            return list;
//        } else {
//            throw new ResourceNotFoundException("This Code System is not part of PLR Code System:" +csId);
//        }
//    }

    // TODO this 2 should be Utils method
    private static boolean searchBy(String param, String csValue) {
        return (param == null) || StringUtils.lowerCase(param).equals(StringUtils.lowerCase(csValue));
    }
      
    private String ifNullParam(StringParam value) {
        return (value != null) ? value.getValue() : null;
    }
}
