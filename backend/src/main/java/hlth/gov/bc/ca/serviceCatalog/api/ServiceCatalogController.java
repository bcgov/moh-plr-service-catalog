package hlth.gov.bc.ca.serviceCatalog.api;

import hlth.gov.bc.ca.serviceCatalog.api.dto.CatalogServiceRequest;
import hlth.gov.bc.ca.serviceCatalog.api.dto.CatalogServiceResponse;
import hlth.gov.bc.ca.serviceCatalog.service.ServiceCatalogAppService;
import jakarta.validation.Valid;
import java.util.List;
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
    
    private final ServiceCatalogAppService serviceCatalogAppService;
    
    public ServiceCatalogController(ServiceCatalogAppService serviceCatalogAppService) {
        this.serviceCatalogAppService = serviceCatalogAppService;
    }
    
    @GetMapping
    public List<CatalogServiceResponse> listServices() {
        return serviceCatalogAppService.listServices();
    }
    
    @PostMapping
    public ResponseEntity<CatalogServiceResponse> createService(
            @Valid @RequestBody CatalogServiceRequest request) {
        CatalogServiceResponse response = serviceCatalogAppService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @DeleteMapping("/{logicalId}")
    public ResponseEntity<Void> deleteService(@PathVariable Long logicalId) {
        serviceCatalogAppService.deleteService(logicalId);
        return ResponseEntity.noContent().build();
    }
}
