package gmky.codebase.service;

import gmky.codebase.api.model.JobRoleResponse;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.mapper.JobRoleMapper;
import gmky.codebase.model.entity.JobRole;
import gmky.codebase.repository.JobRoleRepository;
import gmky.codebase.service.impl.JobRoleServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JobRoleServiceTest {
    @Mock
    JobRoleRepository jobRoleRepository;

    @Mock
    JobRoleMapper jobRoleMapper;

    @InjectMocks
    JobRoleServiceImpl jobRoleService;

    @Test
    @DisplayName("Get all job roles should OK")
    void testGetAllJobRoles_shouldReturnData() {
        var keyword = "admin";
        var pageable = PageRequest.of(0, 20);
        var jobRole = mockJobRole();
        Mockito.when(jobRoleRepository.findAllByNameStartsWith(keyword, pageable)).thenReturn(new PageImpl<>(List.of(jobRole)));
        Mockito.when(jobRoleMapper.toDto(jobRole)).thenReturn(mockJobRoleResponse());
        var result = jobRoleService.getAllJobRoles(keyword, pageable);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Get job role detail by ID should throw NotFoundException")
    void testGetJobRoleDetailById_shouldThrowNotFoundException() {
        Mockito.when(jobRoleRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> jobRoleService.getJobRoleDetailById(1L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Get job role detail by ID should OK")
    void testGetJobRoleDetailById_shouldOK() {
        var jobRole = mockJobRole();
        Mockito.when(jobRoleRepository.findById(1L)).thenReturn(Optional.of(jobRole));
        Mockito.when(jobRoleMapper.toDto(jobRole)).thenReturn(mockJobRoleResponse());
        var result = jobRoleService.getJobRoleDetailById(1L);
        Assertions.assertThat(result).isNotNull();
    }

    private JobRole mockJobRole() {
        var jobRole = new JobRole();
        jobRole.setName("admin");
        jobRole.setDescription("Administrator");
        return jobRole;
    }

    private JobRoleResponse mockJobRoleResponse() {
        var result = new JobRoleResponse();
        result.setName("admin");
        result.setDescription("Administrator");
        return result;
    }
}
