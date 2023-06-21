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

/**
 *
 * @author camille.estival
 */
@Entity
@Table(name = "ServiceCatalog")
public class ServiceCatalog {
    
    
        @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
    
    public ServiceCatalog() {
    
    }
    
    @Override
    public String toString() {
		return "Tutorial [id=" + id + ", title=" + title + ", desc=" + description +" ]";
    }    
        
}
