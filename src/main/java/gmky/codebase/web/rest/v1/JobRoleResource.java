package gmky.codebase.web.rest.v1;

import gmky.codebase.api.model.JobRoleResponse;
import gmky.codebase.service.JobRoleService;
import gmky.codebase.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import gmky.codebase.api.JobRoleManagementApi;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JobRoleResource implements JobRoleManagementApi {
    private final JobRoleService jobRoleService;

    @Override
    public ResponseEntity<List<JobRoleResponse>> getAllJobRoles(String keyword, Pageable pageable) {
        var page = jobRoleService.getAllJobRoles(keyword, pageable);
        var headers = PaginationUtil.generatePaginationHeader(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<JobRoleResponse> getJobRoleDetail(Long jobRoleId) {
        var jobRole = jobRoleService.getJobRoleDetailById(jobRoleId);
        return ResponseEntity.ok(jobRole);
    }
}
