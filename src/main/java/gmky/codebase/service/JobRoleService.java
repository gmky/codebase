package gmky.codebase.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import gmky.codebase.api.model.JobRoleResponse;

public interface JobRoleService {
    Page<JobRoleResponse> getAllJobRoles(String keyword, Pageable pageable);

    JobRoleResponse getJobRoleDetailById(Long id);
}
