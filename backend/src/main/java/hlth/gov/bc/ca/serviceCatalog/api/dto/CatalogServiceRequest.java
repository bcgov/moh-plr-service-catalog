package hlth.gov.bc.ca.serviceCatalog.api.dto;

import jakarta.validation.constraints.NotBlank;

public class CatalogServiceRequest {
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String description;
    
    private String externalIdentifier;
    
    @NotBlank
    private String systemCode;
    
    private Long parentLogicalId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExternalIdentifier() {
        return externalIdentifier;
    }

    public void setExternalIdentifier(String externalIdentifier) {
        this.externalIdentifier = externalIdentifier;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public Long getParentLogicalId() {
        return parentLogicalId;
    }

    public void setParentLogicalId(Long parentLogicalId) {
        this.parentLogicalId = parentLogicalId;
    }
}
