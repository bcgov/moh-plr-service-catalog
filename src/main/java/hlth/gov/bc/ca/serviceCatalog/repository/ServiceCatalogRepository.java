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
    
    @Query(value="SELECT * FROM plr_hs_catalog.catalog_service s, plr_hs_catalog.hs_system_of_origin syst, plr_hs_catalog.catalog_service parent "
                + "WHERE s.system_id = syst.system_id "
                + "AND s.parent_service_id = parent.catalog_service_id "
                + "AND (:name is null OR s.service_name ILIKE %:name%) "
                + "AND (:systemCode is null OR syst.system_cd = :systemCode) "
                + "AND (:externalIdentifier is null OR s.service_ext_ident = :externalIdentifier)"
                + "AND (:parentLogicalId is null OR parent.service_logical_id = :parentLogicalId)",
            nativeQuery = true)
    List <ServiceCatalog> searchServiceCatalog(String name, String externalIdentifier, String systemCode, Long parentLogicalId);
   
    
    List <ServiceCatalog> findByName(String name);
    List <ServiceCatalog> findByNameContainingIgnoreCase(String name);
    
    @Query(value="SELECT * FROM plr_hs_catalog.catalog_service s WHERE s.service_name ILIKE %:name%", nativeQuery = true)
    List <ServiceCatalog> searchByNameNative(@Param("name") String name);
    
    @Query("SELECT s FROM ServiceCatalog s WHERE s.system.code =:systemCode and s.externalIdentifier=:externalIdentifier")
    List <ServiceCatalog> findByExternalIdentifierAndSystem(String externalIdentifier, String systemCode);
    
//    @Query("SELECT s FROM plr_hs_catalog.catalog_service cat, plr_hs_catalog.hs_system_of_origin syst WHERE syst.system_id = cat.system_id and syst.system_cd =:systemCode")
    @Query("SELECT s FROM ServiceCatalog s WHERE s.system.code = :systemCode")
    List <ServiceCatalog> findBySystemCode(String systemCode);
    
    @Query("SELECT s FROM ServiceCatalog s, SpecialtyRelationship sr WHERE s.serviceId = sr.service.serviceId AND sr.lookupCode = :specialtyCode")
    List <ServiceCatalog> findBySpecialty(String specialtyCode);
    
    @Query("SELECT s FROM ServiceCatalog s, ServiceTypeRelationship str WHERE s.serviceId = str.service.serviceId AND str.lookupCode = :typeCode")
    List <ServiceCatalog> findByServiceType(String typeCode);
    

//    List <ServiceCatalog> findBySpecialtyContaining(String specialtyString);
//    List <ServiceCatalog> findByServiceTypeContaining(String typeString);
    
}

