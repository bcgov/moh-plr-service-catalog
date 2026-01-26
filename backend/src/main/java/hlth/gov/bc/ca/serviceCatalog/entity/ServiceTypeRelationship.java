package hlth.gov.bc.ca.serviceCatalog.entity;

import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
 * Entity Class to represent the DB table service_type_rel, which stores the relationships between 
 * a catalog_service and service type (service_type_lookup_cd) from a given code System (code_system_id)
 * @author camille.estival
 */
@Entity
@Table(name = "service_type_rel", schema="plr_hs_catalog")
 @Builder(toBuilder = true)
 @AllArgsConstructor(access = AccessLevel.PACKAGE)
 @NoArgsConstructor(access = AccessLevel.PACKAGE)
 @Setter(value = AccessLevel.PACKAGE)
 @Getter
public class ServiceTypeRelationship {
    
    @Id
    @Column(name = "service_type_rel_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long typeRelationId;

    @Column(name = "service_type_lookup_cd")
    private String lookupCode;

    @OneToOne()
    @JoinColumn(name = "code_system_id")
    private CodeSystem codeSystem;
    
    @ManyToOne
    @JoinColumn(name = "catalog_service_id")
    private ServiceCatalog service;

    @Column(name = "effective_start_dt")
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "effective_end_dt")
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    
    @Override
    public String toString() {
	return "ServiceTypeRelationship [id=" + typeRelationId 
                + ", code=" + lookupCode 
                + ", serviceID="  
                + ", codeSystemURL=" 
                + codeSystem.getSystemUrl()+" ]";
    }    
}
