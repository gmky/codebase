package gmky.codebase.repository;

import gmky.codebase.model.entity.FunctionPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionPrivilegeRepository extends JpaRepository<FunctionPrivilege, Long> {
}
