package hlth.gov.bc.ca.serviceCatalog.api;

import hlth.gov.bc.ca.serviceCatalog.api.dto.ClientLogRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client-logs")
public class ClientLogController {

    private static final Logger log = LoggerFactory.getLogger(ClientLogController.class);
    private static final Logger clientLog = LoggerFactory.getLogger("client-logs");

    @PostMapping
    public ResponseEntity<Void> createClientLog(@Valid @RequestBody ClientLogRequest request) {
        String message = request.getMessage().trim();
        String context = normalize(request.getContext());
        String payload = context == null ? message : message + " | context=" + context;
        String level = normalize(request.getLevel());

        if (level == null) {
            clientLog.info(payload);
        } else {
            switch (level.toLowerCase()) {
                case "debug":
                    clientLog.debug(payload);
                    break;
                case "info":
                    clientLog.info(payload);
                    break;
                case "warn":
                case "warning":
                    clientLog.warn(payload);
                    break;
                case "error":
                    clientLog.error(payload);
                    break;
                default:
                    clientLog.info("level={} {}", level, payload);
                    break;
            }
        }

        log.debug("Client log accepted level={}", level == null ? "info" : level);
        return ResponseEntity.noContent().build();
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
