package hlth.gov.bc.ca.serviceCatalog.api.dto;

import java.time.LocalDate;

public class CodeSystemResponse {
    private Long id;
    private String description;
    private String systemUrl;
    private LocalDate startDate;
    private LocalDate endDate;

    public CodeSystemResponse(
            Long id,
            String description,
            String systemUrl,
            LocalDate startDate,
            LocalDate endDate) {
        this.id = id;
        this.description = description;
        this.systemUrl = systemUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
