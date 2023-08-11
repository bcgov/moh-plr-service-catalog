package hlth.gov.bc.ca.serviceCatalog.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * Entity Class to represent the DB table hs_system_of_origin, used to define the system that any Catalog Service comes from
 * @author camille.estival
 */
@Entity
@Table(name = "hs_system_of_origin", schema="plr_hs_catalog")
 @Builder(toBuilder = true)
 @AllArgsConstructor(access = AccessLevel.PACKAGE)
 @NoArgsConstructor(access = AccessLevel.PACKAGE)
 @Setter(value = AccessLevel.PACKAGE)
 @Getter
public class SystemOfOrigin {
    
    @Id
    @Column(name = "system_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long systemId;

    @Column(name = "system_cd")
    private String code;

    @Column(name = "system_desc_txt")
    private String description;
    
    @Column(name = "effective_start_dt")
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "effective_end_dt")
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    
    @Override
    public String toString() {
        return "SystemOfOrigin [id=" + systemId + ", code=" + code + ", desc=" + description +" ]";
    }    
        
}
