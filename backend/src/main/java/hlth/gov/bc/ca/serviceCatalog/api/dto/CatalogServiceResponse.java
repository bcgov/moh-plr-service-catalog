package hlth.gov.bc.ca.serviceCatalog.api.dto;

public class CatalogServiceResponse {
    
    private Long logicalId;
    private String name;
    private String description;
    private String externalIdentifier;
    private String systemCode;
    private Long parentLogicalId;

    public CatalogServiceResponse() {}

    public CatalogServiceResponse(
            Long logicalId,
            String name,
            String description,
            String externalIdentifier,
            String systemCode,
            Long parentLogicalId) {
        this.logicalId = logicalId;
        this.name = name;
        this.description = description;
        this.externalIdentifier = externalIdentifier;
        this.systemCode = systemCode;
        this.parentLogicalId = parentLogicalId;
    }

    public Long getLogicalId() {
        return logicalId;
    }

    public void setLogicalId(Long logicalId) {
        this.logicalId = logicalId;
    }

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
