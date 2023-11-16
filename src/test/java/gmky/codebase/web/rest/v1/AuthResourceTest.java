package gmky.codebase.web.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import gmky.codebase.api.model.LoginReq;
import gmky.codebase.service.UserService;
import gmky.codebase.service.impl.AuthServiceImpl;
import gmky.codebase.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class AuthResourceTest {
    private static final String LOGIN_URI = "/client-api/v1/auth/login";
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

    private LoginReq mockLoginReq() {
        var req = new LoginReq();
        req.setUsername(USERNAME);
        req.setPassword(PASSWORD);
        return req;
    }
}
