/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hlth.gov.bc.ca.serviceCatalog.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author camille.estival
 */
@Entity
@Table(name = "catalog_service", schema="plr_hs_catalog")
@Getter
@Setter
public class ServiceCatalog {
    
    
    @Id
    @Column(name = "catalog_service_id")
    private long serviceId;
    
    @Column(name = "service_logical_id")
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private long logicalId;
    
    @Column(name = "service_ext_ident")
    private String externalIdentifier;
                
    @Column(name = "service_name")
    private String name;

    @Column(name = "service_desc_txt")
    private String description;
    
//    @Column(name = "system_id")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "system_id")
    private SystemOfOrigin system;

//    @Column(name = "parent_service_id")    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_service_id")
    private ServiceCatalog parentService;
    
    @Column(name = "notes_txt")
    private String notes;

    @Column(name = "comments_txt")
    private String comments;
    
    @Column(name = "effective_start_dt")
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "effective_end_dt")
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    
    public ServiceCatalog() {
    
    }
    
    @Override
    public String toString() {
		return "Service [id=" + logicalId + ",ext_id=" + externalIdentifier + ", name=" + name + ", desc=" + description +" ]";
    }    
        
}
