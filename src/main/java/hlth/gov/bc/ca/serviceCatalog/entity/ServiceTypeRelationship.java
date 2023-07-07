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

/**
 *
 * @author camille.estival
 */
@Entity
@Table(name = "service_type_rel", schema="plr_hs_catalog")
@Getter
public class ServiceTypeRelationship {
    
    @Id
    @Column(name = "service_type_rel_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long typeRelationId;

    @Column(name = "service_type_lookup_cd")
    private String lookupCode;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code_system_id")
    private SystemOfOrigin codeSystem;
    
    @OneToOne() // lazy as we will not use this relationship from there
    @JoinColumn(name = "catalog_service_id")
    private ServiceCatalog service;

    @Column(name = "effective_start_dt")
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "effective_end_dt")
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    
    
    public ServiceTypeRelationship() {
    
    }
    
    @Override
    public String toString() {
		return "ServiceTypeRelationship [id=" + typeRelationId + ", code=" + lookupCode + ", serviceID="  +", codeSystemURL=" + codeSystem.getCode() +" ]";
    }    
        
}
