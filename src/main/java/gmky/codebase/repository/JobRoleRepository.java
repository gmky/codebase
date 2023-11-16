package gmky.codebase.repository;

import gmky.codebase.model.entity.JobRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {
    Page<JobRole> findAllByNameStartsWith(String keyword, Pageable pageable);
}
