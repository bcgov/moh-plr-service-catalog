package hlth.gov.bc.ca.serviceCatalog.api.dto;

import jakarta.validation.constraints.NotBlank;

public class ClientLogRequest {

    @NotBlank
    private String message;

    private String level;

    private String context;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
