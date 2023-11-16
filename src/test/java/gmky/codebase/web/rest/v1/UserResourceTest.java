package gmky.codebase.web.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import gmky.codebase.api.model.CreateUserReq;
import gmky.codebase.api.model.UpdateUserReq;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.enumeration.UserStatusEnum;
import gmky.codebase.service.impl.UserServiceImpl;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {
    private static final String USERNAME = "admin";
    private static final String EMAIL = "admin@gmky.dev";
    private static final String FULL_NAME = "Vu Hoang Hiep";
    private static final String CREATE_USER_URI = "/client-api/v1/users";
    private static final String GET_ALL_USERS_URI = "/client-api/v1/users";
    private static final String GET_USER_DETAIL_BY_ID_URI = "/client-api/v1/users/1";
    private static final String UPDATE_USER_DETAIL_BY_ID_URI = "/client-api/v1/users/1";

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(new UserResource(userService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("Create user should OK")
    void testCreateUser_shouldOk() throws Exception {
        var req = mockCreateUserReq();
        mockMvc.perform(post(CREATE_USER_URI).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all users should OK")
    void testGetAllUsers_shouldOK() throws Exception {
        Mockito.when(userService.getAllUsers("admin", PageRequest.of(0, 20))).thenReturn(new PageImpl<>(List.of()));
        mockMvc.perform(get(GET_ALL_USERS_URI)
                        .param("keyword", "admin"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get user detail by ID should OK")
    void testGetUserDetailById_shouldOK() throws Exception {
        Mockito.when(userService.getDetailById(1L)).thenReturn(mockUserResponse());
        mockMvc.perform(get(GET_USER_DETAIL_BY_ID_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update user by ID should OK")
    void testUpdateUserById_shouldOK() throws Exception {
        Mockito.when(userService.updateUserById(1L, mockUpdateUserReq())).thenReturn(mockUserResponse());
        mockMvc.perform(put(UPDATE_USER_DETAIL_BY_ID_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(mockUpdateUserReq())))
                .andExpect(status().isOk());
    }

    private CreateUserReq mockCreateUserReq() {
        var req = new CreateUserReq();
        req.setUsername(USERNAME);
        req.setEmail(EMAIL);
        req.setFullName(FULL_NAME);
        req.setStatus(UserStatusEnum.ACTIVE);
        return req;
    }

    private UpdateUserReq mockUpdateUserReq() {
        var req = new UpdateUserReq();
        req.setUsername(USERNAME);
        req.setEmail(EMAIL);
        return req;
    }

    private UserResponse mockUserResponse() {
        var res = new UserResponse();
        res.setUsername(USERNAME);
        res.setEmail(EMAIL);
        return res;
    }
}
