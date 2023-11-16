package gmky.codebase.service;

import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.exception.UnauthorizedException;
import gmky.codebase.model.entity.User;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.security.TokenProvider;
import gmky.codebase.service.impl.AuthServiceImpl;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private static final String USERNAME = "admin";
    private static final String RAW_PASSWORD = "b15dcpt082";
    private static final String EMAIL = "admin@gmky.dev";
    @Mock
    UserRepository userRepository;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AuthServiceImpl authService;

    @Test
    @DisplayName("Login should OK")
    void login_shouldReturnToken() {
        var user = mockUser();
        Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(RAW_PASSWORD, user.getPassword())).thenReturn(true);
        Mockito.when(tokenProvider.generateToken(USERNAME, new HashMap<>())).thenReturn("abc");
        var result = authService.login((new LoginReq()).username(USERNAME).password(RAW_PASSWORD));
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getAccessToken()).isEqualToIgnoringCase("abc");
    }

    @Test
    @DisplayName("Login should throw NotFoundException")
    void login_shouldThrowNotFoundException() {
        Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());
        var req = (new LoginReq()).username(USERNAME).password(RAW_PASSWORD);
        Assertions.assertThatThrownBy(() -> authService.login(req)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Login should throw UnauthorizedException")
    void testLogin_shouldThrowUnauthorizedException() {
        var user = mockUser();
        var req = (new LoginReq()).username(USERNAME).password(RAW_PASSWORD);
        Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(RAW_PASSWORD, user.getPassword())).thenReturn(false);
        Assertions.assertThatThrownBy(() -> authService.login(req)).isInstanceOf(UnauthorizedException.class);
    }

    private User mockUser() {
        var user = new User();
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        return user;
    }
}
