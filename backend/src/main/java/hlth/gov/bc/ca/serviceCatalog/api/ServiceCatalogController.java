package hlth.gov.bc.ca.serviceCatalog.api;

import hlth.gov.bc.ca.serviceCatalog.api.dto.CatalogServiceRequest;
import hlth.gov.bc.ca.serviceCatalog.api.dto.CatalogServiceResponse;
import hlth.gov.bc.ca.serviceCatalog.api.dto.SystemOfOriginResponse;
import hlth.gov.bc.ca.serviceCatalog.service.ServiceCatalogAppService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalog-services")
public class ServiceCatalogController {
    
    private static final Logger log = LoggerFactory.getLogger(ServiceCatalogController.class);

    private final ServiceCatalogAppService serviceCatalogAppService;
    
    public ServiceCatalogController(ServiceCatalogAppService serviceCatalogAppService) {
        this.serviceCatalogAppService = serviceCatalogAppService;
    }
    
    @GetMapping
    public List<CatalogServiceResponse> listServices() {
        log.info("Listing catalog services");
        List<CatalogServiceResponse> responses = serviceCatalogAppService.listServices();
        log.debug("Catalog services returned: {}", responses.size());
        return responses;
    }

    @GetMapping("/systems")
    public List<SystemOfOriginResponse> listSystems() {
        log.info("Listing system of origin codes");
        List<SystemOfOriginResponse> responses = serviceCatalogAppService.listSystems();
        log.debug("System of origin codes returned: {}", responses.size());
        return responses;
    }
    
    @PostMapping
    public ResponseEntity<CatalogServiceResponse> createService(
            @Valid @RequestBody CatalogServiceRequest request) {
        log.info("Creating catalog service");
        log.debug("Create request received for service name={}, systemCode={}", request.getName(), request.getSystemCode());
        CatalogServiceResponse response = serviceCatalogAppService.createService(request);
        log.info("Catalog service created: logicalId={}", response.getLogicalId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @DeleteMapping("/{logicalId}")
    public ResponseEntity<Void> deleteService(@PathVariable Long logicalId) {
        log.info("Deleting catalog service logicalId={}", logicalId);
        serviceCatalogAppService.deleteService(logicalId);
        log.info("Catalog service deleted logicalId={}", logicalId);
        return ResponseEntity.noContent().build();
    }
}
