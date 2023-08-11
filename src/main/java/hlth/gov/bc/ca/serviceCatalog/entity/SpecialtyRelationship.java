package hlth.gov.bc.ca.serviceCatalog.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity Class to represent the DB table specialty_rel, which stores the relationships between 
 * a catalog_service and specialty (specialty_lookup_cd) from a given code System (code_system_id)
 * @author camille.estival
 */
@Entity
@Table(name = "specialty_rel", schema="plr_hs_catalog")
 @Builder(toBuilder = true)
 @AllArgsConstructor(access = AccessLevel.PACKAGE)
 @NoArgsConstructor(access = AccessLevel.PACKAGE)
 @Setter(value = AccessLevel.PACKAGE)
 @Getter
public class SpecialtyRelationship {
    
    @Id
    @Column(name = "specialty_rel_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long specialtyRelationId;

    @Column(name = "specialty_lookup_cd")
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
	return "SpecialtyRelationship [id=" + specialtyRelationId 
                + ", code=" + lookupCode 
                + ", serviceID=" 
                + ", codeSystemURL=" + codeSystem.getSystemUrl() +" ]";
    }    
}
