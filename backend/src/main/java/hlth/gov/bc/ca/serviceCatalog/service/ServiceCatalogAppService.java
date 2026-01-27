package hlth.gov.bc.ca.serviceCatalog.service;

import hlth.gov.bc.ca.serviceCatalog.api.dto.CatalogServiceRequest;
import hlth.gov.bc.ca.serviceCatalog.api.dto.CatalogServiceResponse;
import hlth.gov.bc.ca.serviceCatalog.api.dto.CodeSystemRequest;
import hlth.gov.bc.ca.serviceCatalog.api.dto.CodeSystemResponse;
import hlth.gov.bc.ca.serviceCatalog.api.dto.SystemOfOriginResponse;
import hlth.gov.bc.ca.serviceCatalog.entity.CodeSystem;
import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import hlth.gov.bc.ca.serviceCatalog.entity.SystemOfOrigin;
import hlth.gov.bc.ca.serviceCatalog.repository.CodeSystemRepository;
import hlth.gov.bc.ca.serviceCatalog.repository.ServiceCatalogRepository;
import hlth.gov.bc.ca.serviceCatalog.repository.SystemOfOriginRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServiceCatalogAppService {
    
    private static final Logger log = LoggerFactory.getLogger(ServiceCatalogAppService.class);

    private final ServiceCatalogRepository serviceCatalogRepository;
    private final SystemOfOriginRepository systemOfOriginRepository;
    private final CodeSystemRepository codeSystemRepository;
    private final EntityManager entityManager;
    
    public ServiceCatalogAppService(
            ServiceCatalogRepository serviceCatalogRepository,
            SystemOfOriginRepository systemOfOriginRepository,
            CodeSystemRepository codeSystemRepository,
            EntityManager entityManager) {
        this.serviceCatalogRepository = serviceCatalogRepository;
        this.systemOfOriginRepository = systemOfOriginRepository;
        this.codeSystemRepository = codeSystemRepository;
        this.entityManager = entityManager;
    }
    
    public List<CatalogServiceResponse> listServices() {
        List<CatalogServiceResponse> responses = serviceCatalogRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        log.debug("Loaded {} catalog services", responses.size());
        return responses;
    }

    public List<SystemOfOriginResponse> listSystems() {
        List<SystemOfOriginResponse> responses = systemOfOriginRepository.findAll()
                .stream()
                .map(system -> new SystemOfOriginResponse(
                        system.getSystemId(),
                        system.getCode(),
                        system.getDescription()))
                .collect(Collectors.toList());
        log.debug("Loaded {} system of origin codes", responses.size());
        return responses;
    }

    public List<CodeSystemResponse> listCodeSystems() {
        List<CodeSystemResponse> responses = codeSystemRepository.findAll()
                .stream()
                .map(this::toCodeSystemResponse)
                .collect(Collectors.toList());
        log.debug("Loaded {} code systems", responses.size());
        return responses;
    }
    
    @Transactional
    public CatalogServiceResponse createService(CatalogServiceRequest request) {
        log.info("Creating catalog service name={}, systemCode={}",
                request.getName(), request.getSystemCode());
        SystemOfOrigin system = systemOfOriginRepository.findByCode(request.getSystemCode());
        if (system == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown system code.");
        }
        
        ServiceCatalog parentService = null;
        if (request.getParentLogicalId() != null) {
            parentService = serviceCatalogRepository.findByLogicalId(request.getParentLogicalId());
            if (parentService == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parent service not found.");
            }
        }
        
        Long serviceId = nextVal("PLR_HS_CATALOG.CATALOG_SERVICE_SEQ");
        Long logicalId = nextVal("PLR_HS_CATALOG.SERVICE_LOGICAL_SEQ");
        
        ServiceCatalog service = ServiceCatalog.builder()
                .serviceId(serviceId)
                .logicalId(logicalId)
                .externalIdentifier(normalize(request.getExternalIdentifier()))
                .name(request.getName().trim())
                .description(request.getDescription().trim())
                .system(system)
                .parentService(parentService)
                .startDate(new Date())
                .endDate(null)
                .build();
        
        ServiceCatalog saved = serviceCatalogRepository.save(service);
        log.info("Catalog service created logicalId={}", saved.getLogicalId());
        return toResponse(saved);
    }
    
    @Transactional
    public void deleteService(Long logicalId) {
        log.info("Deleting catalog service logicalId={}", logicalId);
        ServiceCatalog service = serviceCatalogRepository.findByLogicalId(logicalId);
        if (service == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found.");
        }
        Long serviceId = service.getServiceId();
        entityManager.createNativeQuery(
                "DELETE FROM plr_hs_catalog.service_type_rel WHERE catalog_service_id = :id")
                .setParameter("id", serviceId)
                .executeUpdate();
        entityManager.createNativeQuery(
                "DELETE FROM plr_hs_catalog.specialty_rel WHERE catalog_service_id = :id")
                .setParameter("id", serviceId)
                .executeUpdate();
        entityManager.createNativeQuery(
                "DELETE FROM plr_hs_catalog.catalog_service WHERE catalog_service_id = :id")
                .setParameter("id", serviceId)
                .executeUpdate();
        log.debug("Catalog service physical delete completed for logicalId={}, serviceId={}", logicalId, serviceId);
    }

    @Transactional
    public CodeSystemResponse createCodeSystem(CodeSystemRequest request) {
        log.info("Creating code system description={}", request.getDescription());
        Date startDate = resolveStartDate(request);
        Date endDate = resolveEndDate(request, startDate);

        Long codeSystemId = nextVal("PLR_HS_CATALOG.CODE_SYSTEM_SEQ");
        CodeSystem codeSystem = CodeSystem.builder()
                .codeSystemId(codeSystemId)
                .description(request.getDescription().trim())
                .systemUrl(normalize(request.getSystemUrl()))
                .startDate(startDate)
                .endDate(endDate)
                .build();

        CodeSystem saved = codeSystemRepository.save(codeSystem);
        log.info("Code system created id={}", saved.getCodeSystemId());
        return toCodeSystemResponse(saved);
    }

    @Transactional
    public CodeSystemResponse updateCodeSystem(Long codeSystemId, CodeSystemRequest request) {
        log.info("Updating code system id={}", codeSystemId);
        CodeSystem codeSystem = codeSystemRepository.findById(codeSystemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Code system not found."));

        Date startDate = resolveStartDate(request);
        Date endDate = resolveEndDate(request, startDate);

        codeSystem = codeSystem.toBuilder()
                .description(request.getDescription().trim())
                .systemUrl(normalize(request.getSystemUrl()))
                .startDate(startDate)
                .endDate(endDate)
                .build();

        CodeSystem saved = codeSystemRepository.save(codeSystem);
        log.info("Code system updated id={}", saved.getCodeSystemId());
        return toCodeSystemResponse(saved);
    }

    @Transactional
    public void deleteCodeSystem(Long codeSystemId) {
        log.info("Deleting code system id={}", codeSystemId);
        CodeSystem codeSystem = codeSystemRepository.findById(codeSystemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Code system not found."));
        try {
            codeSystemRepository.delete(codeSystem);
            log.info("Code system deleted id={}", codeSystemId);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Code system is referenced by other records and cannot be removed.");
        }
    }
    
    private Long nextVal(String sequenceName) {
        log.debug("Fetching next value from sequence {}", sequenceName);
        Object result = entityManager.createNativeQuery("select nextval('" + sequenceName + "')")
                .getSingleResult();
        return ((Number) result).longValue();
    }
    
    private CatalogServiceResponse toResponse(ServiceCatalog service) {
        Long parentLogicalId = service.getParentService() != null
                ? service.getParentService().getLogicalId()
                : null;
        String systemCode = service.getSystem() != null ? service.getSystem().getCode() : null;
        return new CatalogServiceResponse(
                service.getLogicalId(),
                service.getName(),
                service.getDescription(),
                service.getExternalIdentifier(),
                systemCode,
                parentLogicalId);
    }

    private CodeSystemResponse toCodeSystemResponse(CodeSystem codeSystem) {
        return new CodeSystemResponse(
                codeSystem.getCodeSystemId(),
                codeSystem.getDescription(),
                codeSystem.getSystemUrl(),
                toLocalDate(codeSystem.getStartDate()),
                toLocalDate(codeSystem.getEndDate()));
    }

    private Date resolveStartDate(CodeSystemRequest request) {
        if (request.getStartDate() == null) {
            return new Date();
        }
        return java.sql.Date.valueOf(request.getStartDate());
    }

    private Date resolveEndDate(CodeSystemRequest request, Date startDate) {
        if (request.getEndDate() == null) {
            return null;
        }
        Date endDate = java.sql.Date.valueOf(request.getEndDate());
        if (endDate.before(startDate)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be before start date.");
        }
        return endDate;
    }

    private java.time.LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
    
    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
