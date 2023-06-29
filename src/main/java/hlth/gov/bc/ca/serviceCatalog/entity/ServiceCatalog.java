/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hlth.gov.bc.ca.serviceCatalog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

/**
 *
 * @author camille.estival
 */
@Entity
@Table(name = "catalog_service")
@Getter
public class ServiceCatalog {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_logical_id")
    private long logicalId;

    @Column(name = "service_name")
    private String title;

    @Column(name = "service_desc_txt")
    private String description;
    
    public ServiceCatalog() {
    
    }
    
    @Override
    public String toString() {
		return "Service [id=" + logicalId + ", title=" + title + ", desc=" + description +" ]";
    }    
        
}
