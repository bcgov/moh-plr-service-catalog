/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import lombok.Getter;

/**
 *
 * @author camille.estival
 */
@Entity
@Table(name = "hs_system_of_origin", schema="plr_hs_catalog")
@Getter
public class SystemOfOrigin {
    
    
    @Id
    @Column(name = "system_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long systemId;

    @Column(name = "system_cd")
    private String systemCode;

    @Column(name = "system_desc_txt")
    private String description;
    
    @Column(name = "system_lookup_url")
    private String systemUrl;

    @Column(name = "effective_start_dt")
    @Temporal(value = TemporalType.DATE)
    private Date startDate;
    
    @Column(name = "effective_end_dt")
    @Temporal(value = TemporalType.DATE)
    private Date endDate;
    
    
    public SystemOfOrigin() {
    
    }
    
    @Override
    public String toString() {
		return "Tutorial [id=" + systemId + ", code=" + systemCode + ", desc=" + description +", URL=" + systemUrl +" ]";
    }    
        
}
