package hlth.gov.bc.ca.serviceCatalog.repository;

import hlth.gov.bc.ca.serviceCatalog.entity.ServiceCatalog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository Class to retrieve ServiceCatalog from the DB
 * @author camille.estival
 */
@Repository
public interface ServiceCatalogRepository extends JpaRepository<ServiceCatalog, Long> {
    
    /**
     * Find ServiceCatalog by LogicalId
     * @param logicalId
     * @return 
     */
    ServiceCatalog findByLogicalId(long logicalId);
    
    /**
     * Search ServiceCatalog by name (ignoreCase and wildcard), System and/or externalIdentifier
     * @param name
     * @param externalIdentifier
     * @param systemCode
     * @return 
     */
    @Query(value="SELECT * FROM plr_hs_catalog.catalog_service s, plr_hs_catalog.hs_system_of_origin syst "
                + "WHERE s.system_id = syst.system_id "
                + "AND (:name is null OR s.service_name ILIKE %:name%) "
                + "AND (:systemCode is null OR syst.system_cd = :systemCode) "
                + "AND (:externalIdentifier is null OR s.service_ext_ident = :externalIdentifier)",
            nativeQuery = true)
    List <ServiceCatalog> searchServiceCatalog(String name, String externalIdentifier, String systemCode);

    
    /**
     * Find ServiceCatalog by ExternalIdentifier and System - should be unique!
     * @param externalIdentifier
     * @param systemCode
     * @return 
     */
    @Query("SELECT s FROM ServiceCatalog s WHERE s.system.code =:systemCode and s.externalIdentifier=:externalIdentifier")
    List <ServiceCatalog> findByExternalIdentifierAndSystem(String externalIdentifier, String systemCode);

}

