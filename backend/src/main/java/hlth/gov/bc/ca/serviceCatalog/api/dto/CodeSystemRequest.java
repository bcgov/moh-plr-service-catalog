package hlth.gov.bc.ca.serviceCatalog.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CodeSystemRequest {

    @NotBlank
    private String description;

    private String systemUrl;

    private LocalDate startDate;

    private LocalDate endDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(String systemUrl) {
        this.systemUrl = systemUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
