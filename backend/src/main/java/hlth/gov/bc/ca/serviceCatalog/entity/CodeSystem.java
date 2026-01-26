package hlth.gov.bc.ca.serviceCatalog.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity Class to represent the DB table Code_System, used to define specialty and serviceType code sets 
 * @author camille.estival
 */
@Entity
@Table(name = "code_system", schema= "plr_hs_catalog")
 @Builder(toBuilder = true)
 @AllArgsConstructor(access = AccessLevel.PACKAGE)
 @NoArgsConstructor(access = AccessLevel.PACKAGE)
 @Setter(value = AccessLevel.PACKAGE)
 @Getter
public class CodeSystem {
    
    @Id
    @Column(name = "code_system_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long codeSystemId;

    @Column(name = "code_system_desc_txt")
    private String description;
    
    @Column(name = "code_system_lookup_url")
    private String systemUrl;

    @Column(name = "effective_start_dt")
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "effective_end_dt")
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    
    @Override
    public String toString() {
		return "SystemOfOrigin [id=" + codeSystemId + ", desc=" + description +", URL=" + systemUrl +" ]";
    }    
        
}
