/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hlth.gov.bc.ca.serviceCatalog.repository;

import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author camille.estival
 */
@Repository
public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    
    ServiceCatalog findByLogicalId(long logicalId);
    List <ServiceCatalog> findByName(String name);
    
    ServiceCatalog findByExternalIdentifierAndSystem(String externalIdentifier, String systemCode);
    List <ServiceCatalog> findBySystemCode(String systemCode);
//    List <ServiceCatalog> findBySpecialty(String specialtyCode);
//    List <ServiceCatalog> findByServiceType(String typeCode);
//    List <ServiceCatalog> findByParent(String parentLogicalId);
    
    List <ServiceCatalog> findByNameContaining(String name);
//    List <ServiceCatalog> findBySpecialtyContaining(String specialtyString);
//    List <ServiceCatalog> findByServiceTypeContaining(String typeString);
    
}

