package gmky.codebase.service.impl;

import gmky.codebase.api.model.JobRoleResponse;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.mapper.JobRoleMapper;
import gmky.codebase.repository.JobRoleRepository;
import gmky.codebase.service.JobRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobRoleServiceImpl implements JobRoleService {
    private final JobRoleRepository jobRoleRepository;
    private final JobRoleMapper jobRoleMapper;

    @Override
    public Page<JobRoleResponse> getAllJobRoles(String keyword, Pageable pageable) {
        var page = jobRoleRepository.findAllByNameStartsWith(keyword, pageable);
        return page.map(jobRoleMapper::toDto);
    }

    @Override
    public JobRoleResponse getJobRoleDetailById(Long id) {
        var jobRole = jobRoleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Job role not found"));
        return jobRoleMapper.toDto(jobRole);
    }
}
