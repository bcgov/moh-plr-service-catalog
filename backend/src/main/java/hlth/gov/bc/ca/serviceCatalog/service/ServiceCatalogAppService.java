package hlth.gov.bc.ca.serviceCatalog.service;

import hlth.gov.bc.ca.serviceCatalog.api.dto.CatalogServiceRequest;
import hlth.gov.bc.ca.serviceCatalog.api.dto.CatalogServiceResponse;
import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import hlth.gov.bc.ca.serviceCatalog.entity.SystemOfOrigin;
import hlth.gov.bc.ca.serviceCatalog.repository.ServiceCatalogRepository;
import hlth.gov.bc.ca.serviceCatalog.repository.SystemOfOriginRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServiceCatalogAppService {
    
    private final ServiceCatalogRepository serviceCatalogRepository;
    private final SystemOfOriginRepository systemOfOriginRepository;
    private final EntityManager entityManager;
    
    public ServiceCatalogAppService(
            ServiceCatalogRepository serviceCatalogRepository,
            SystemOfOriginRepository systemOfOriginRepository,
            EntityManager entityManager) {
        this.serviceCatalogRepository = serviceCatalogRepository;
        this.systemOfOriginRepository = systemOfOriginRepository;
        this.entityManager = entityManager;
    }
    
    public List<CatalogServiceResponse> listServices() {
        return serviceCatalogRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CatalogServiceResponse createService(CatalogServiceRequest request) {
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
        return toResponse(saved);
    }
    
    @Transactional
    public void deleteService(Long logicalId) {
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
    }
    
    private Long nextVal(String sequenceName) {
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
    
    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
