package hlth.gov.bc.ca.serviceCatalog.repository;

import hlth.gov.bc.ca.serviceCatalog.entity.SystemOfOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemOfOriginRepository extends JpaRepository<SystemOfOrigin, Long> {
    
    SystemOfOrigin findByCode(String code);
}
