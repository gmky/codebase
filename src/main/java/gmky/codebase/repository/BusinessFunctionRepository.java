package gmky.codebase.repository;

import gmky.codebase.model.entity.BusinessFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessFunctionRepository extends JpaRepository<BusinessFunction, Long> {
}
