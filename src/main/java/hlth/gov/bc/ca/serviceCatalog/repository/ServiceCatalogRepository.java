/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hlth.gov.bc.ca.serviceCatalog.repository;

import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author camille.estival
 */
@Repository
public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    
    ServiceCatalog findByLogicalId(long logicalId);
    List <ServiceCatalog> findByName(String name);
    
    @Query("SELECT s FROM ServiceCatalog s WHERE (:systemCode is null OR s.system.code =:systemCode) "
            + "and (:externalIdentifier is null OR s.externalIdentifier=:externalIdentifier)")
    List <ServiceCatalog> searchServiceCatalog(String externalIdentifier, String systemCode);
    
    @Query("SELECT s FROM ServiceCatalog s WHERE s.system.code =:systemCode and s.externalIdentifier=:externalIdentifier")
    List <ServiceCatalog> findByExternalIdentifierAndSystem(String externalIdentifier, String systemCode);
    
//    @Query("SELECT s FROM plr_hs_catalog.catalog_service cat, plr_hs_catalog.hs_system_of_origin syst WHERE syst.system_id = cCat.system_id and syst.system_cd =:systemCode")
    @Query("SELECT s FROM ServiceCatalog s WHERE s.system.code = :systemCode")
    List <ServiceCatalog> findBySystemCode(String systemCode);
    
    @Query("SELECT s FROM ServiceCatalog s, SpecialtyRelationship sr WHERE s.serviceId = sr.service.serviceId AND sr.lookupCode = :specialtyCode")
    List <ServiceCatalog> findBySpecialty(String specialtyCode);
    
    @Query("SELECT s FROM ServiceCatalog s, ServiceTypeRelationship str WHERE s.serviceId = str.service.serviceId AND str.lookupCode = :typeCode")
    List <ServiceCatalog> findByServiceType(String typeCode);
    
    // TODO need recursive search by parent? not sure, they can drill down to see more children
    @Query("SELECT s FROM ServiceCatalog s WHERE s.parentService.logicalId = :parentLogicalId")
    List <ServiceCatalog> findByParent(Long parentLogicalId);
    
//    List <ServiceCatalog> findAllIgnoreCase(Example <ServiceCatalog> criteria);
    
    List <ServiceCatalog> findByNameContainingIgnoreCase(String name);
    @Query(value="SELECT * FROM plr_hs_catalog.catalog_service s WHERE s.service_name ILIKE %:name%", nativeQuery = true)
    List <ServiceCatalog> searchByNameNative(@Param("name") String name);
//    List <ServiceCatalog> findBySpecialtyContaining(String specialtyString);
//    List <ServiceCatalog> findByServiceTypeContaining(String typeString);
    
}

