package hlth.gov.bc.ca.serviceCatalog.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
import lombok.Singular;

/**
 * Entity Class to represent the DB table catalog_service, which store services of type Catalogue 
 * (aka just a definition of a service, not an instantiation of a service at a specific organization/clinic) 
 * @author camille.estival
 */
@Entity
@Table(name = "catalog_service", schema="plr_hs_catalog")
 @Builder(toBuilder = true)
 @AllArgsConstructor(access = AccessLevel.PACKAGE)
 @NoArgsConstructor(access = AccessLevel.PACKAGE)
 @Setter(value = AccessLevel.PACKAGE)
 @Getter
public class ServiceCatalog implements Serializable{
    
    @Id
    @Column(name = "catalog_service_id")
    private Long serviceId;
    
    @Column(name = "service_logical_id")
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long logicalId;
    
    @Column(name = "service_ext_ident")
    private String externalIdentifier;
                
    @Column(name = "service_name")
    private String name;

    @Column(name = "service_desc_txt")
    private String description;
    
    @OneToOne()
    @JoinColumn(name = "system_id")
    private SystemOfOrigin system;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_service_id")
    private ServiceCatalog parentService;
    
    @Column(name = "notes_txt")
    private String notes;

    @Column(name = "comments_txt")
    private String comments;
    
    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER)
    @Singular private Set<ServiceTypeRelationship> serviceTypeRelationships;
    
    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER)
    @Singular private Set<SpecialtyRelationship> specialtyRelationships;
    
    @Column(name = "effective_start_dt")
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "effective_end_dt")
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    
    @Override
    public String toString() {
	return "ServiceCatalog [id=" + logicalId + ",ext_id=" + externalIdentifier + ", name=" + name + ", desc=" + description +" ]";
    }    
        
}
