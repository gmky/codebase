package gmky.codebase.repository;

import gmky.codebase.model.entity.AssignablePermissionSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignablePermissionSetRepository extends JpaRepository<AssignablePermissionSet, Long> {
}
