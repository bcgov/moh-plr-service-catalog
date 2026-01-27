package hlth.gov.bc.ca.serviceCatalog.api;

import hlth.gov.bc.ca.serviceCatalog.api.dto.CodeSystemRequest;
import hlth.gov.bc.ca.serviceCatalog.api.dto.CodeSystemResponse;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/code-systems")
public class CodeSystemController {

    private static final Logger log = LoggerFactory.getLogger(CodeSystemController.class);

    private final ServiceCatalogAppService serviceCatalogAppService;

    public CodeSystemController(ServiceCatalogAppService serviceCatalogAppService) {
        this.serviceCatalogAppService = serviceCatalogAppService;
    }

    @GetMapping
    public List<CodeSystemResponse> listCodeSystems() {
        log.info("Listing code systems");
        List<CodeSystemResponse> responses = serviceCatalogAppService.listCodeSystems();
        log.debug("Code systems returned: {}", responses.size());
        return responses;
    }

    @PostMapping
    public ResponseEntity<CodeSystemResponse> createCodeSystem(
            @Valid @RequestBody CodeSystemRequest request) {
        log.info("Creating code system");
        log.debug("Create request received for code system description={}", request.getDescription());
        CodeSystemResponse response = serviceCatalogAppService.createCodeSystem(request);
        log.info("Code system created: id={}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{codeSystemId}")
    public CodeSystemResponse updateCodeSystem(
            @PathVariable Long codeSystemId,
            @Valid @RequestBody CodeSystemRequest request) {
        log.info("Updating code system id={}", codeSystemId);
        log.debug("Update request received for code system description={}", request.getDescription());
        return serviceCatalogAppService.updateCodeSystem(codeSystemId, request);
    }

    @DeleteMapping("/{codeSystemId}")
    public ResponseEntity<Void> deleteCodeSystem(@PathVariable Long codeSystemId) {
        log.info("Deleting code system id={}", codeSystemId);
        serviceCatalogAppService.deleteCodeSystem(codeSystemId);
        log.info("Code system deleted id={}", codeSystemId);
        return ResponseEntity.noContent().build();
    }
}
