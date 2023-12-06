package gmky.codebase.web.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import gmky.codebase.api.model.ChangePasswordReq;
import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.RegisterUserReq;
import gmky.codebase.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class AuthResourceTest {
    private static final String LOGIN_URI = "/client-api/v1/auth/login";
    private static final String PROFILE_URI = "/client-api/v1/auth/me";
    private static final String SUMMARY_URI = "/client-api/v1/auth/summary";
    private static final String FORGOT_PASSWORD_URI = "/client-api/v1/forgot-password";
    private static final String REGISTER_URI = "/client-api/v1/register";
    private static final String CHANGE_PASSWORD_URI = "/client-api/v1/change-password";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "b15dcpt082";

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        this.mockMvc = standaloneSetup(new AuthResource(authService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    @DisplayName("Login should OK")
    void testLogin_shouldOK() throws Exception {
        mockMvc.perform(post(LOGIN_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockLoginReq())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get profile should OK")
    void testMe_shouldOK() throws Exception {
        mockMvc.perform(get(PROFILE_URI)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get summary should OK")
    void testSummary_shouldOK() throws Exception {
        mockMvc.perform(get(SUMMARY_URI)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Forgot password should OK")
    void testForgotPassword_shouldOK() throws Exception {
        mockMvc.perform(get(FORGOT_PASSWORD_URI)
                        .queryParam("email", "admin@gmky.dev")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Register should OK")
    void testRegister_shouldOK() throws Exception {
        var reqBody = (new RegisterUserReq())
                .username("gmkyx2").email("gmky@gmky.dev")
                .password("b15dcpt082").fullName("Vu Hoang Hiep");
        mockMvc.perform(post(REGISTER_URI)
                        .content(objectMapper.writeValueAsString(reqBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Change password should OK")
    void testChangePassword_shouldOK() throws Exception {
        var reqBody = (new ChangePasswordReq())
                .currentPassword("b15dcpt082")
                .newPassword("b15dcpt0823");
        mockMvc.perform(post(CHANGE_PASSWORD_URI)
                        .content(objectMapper.writeValueAsString(reqBody))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    private LoginReq mockLoginReq() {
        var req = new LoginReq();
        req.setUsername(USERNAME);
        req.setPassword(PASSWORD);
        return req;
    }
}
