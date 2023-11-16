package gmky.codebase.web.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import gmky.codebase.api.model.JobRoleResponse;
import gmky.codebase.service.impl.JobRoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JobRoleResourceTest {
    private static final String GET_ALL_JOB_ROLES_URI = "/client-api/v1/job-roles";
    private static final String GET_JOB_ROLE_DETAIL_URI = "/client-api/v1/job-roles/1";
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    JobRoleServiceImpl jobRoleService;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(new JobRoleResource(jobRoleService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("Get all job roles should OK")
    void testGetAllJobRoles_shouldOK() throws Exception {
        Mockito.when(jobRoleService.getAllJobRoles("admin", PageRequest.of(0, 20))).thenReturn(new PageImpl<>(List.of()));
        mockMvc.perform(get(GET_ALL_JOB_ROLES_URI).param("keyword", "admin"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get job role detail by ID should OK")
    void testGetJobRoleById_shouldOK() throws Exception {
        Mockito.when(jobRoleService.getJobRoleDetailById(1L)).thenReturn(mockJobRoleResponse());
        mockMvc.perform(get(GET_JOB_ROLE_DETAIL_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private JobRoleResponse mockJobRoleResponse() {
        var res = new JobRoleResponse();
        res.setName("admin");
        return res;
    }
}
