package hlth.gov.bc.ca.serviceCatalog.repository;

import hlth.gov.bc.ca.serviceCatalog.entity.CodeSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeSystemRepository extends JpaRepository<CodeSystem, Long> {
}
