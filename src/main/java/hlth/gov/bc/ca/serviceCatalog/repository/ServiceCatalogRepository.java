/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hlth.gov.bc.ca.serviceCatalog.repository;

import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author camille.estival
 */
public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    
    List <ServiceCatalog> findByPublished(boolean published);
    List <ServiceCatalog> findByTitleContaining(String title);
    
}

