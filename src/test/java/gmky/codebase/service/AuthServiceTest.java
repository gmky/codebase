package gmky.codebase.service;

import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.exception.ForbiddenException;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.exception.UnauthorizedException;
import gmky.codebase.mapper.UserMapper;
import gmky.codebase.model.entity.User;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.security.TokenProvider;
import gmky.codebase.service.impl.AuthServiceImpl;
import gmky.codebase.util.SecurityUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
    UserMapper userMapper;

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

    @Test
    @DisplayName("Get profile should throw ForbiddenException")
    void testMe_shouldThrowForbiddenException() {
        try (MockedStatic<SecurityUtil> util = Mockito.mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
            Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());
            Assertions.assertThatThrownBy(() -> authService.me()).isInstanceOf(ForbiddenException.class);
        }
    }

    @Test
    @DisplayName("Get profile should return data")
    void testMe_shouldOK() {
        try (MockedStatic<SecurityUtil> util = Mockito.mockStatic(SecurityUtil.class)) {
            var user = mockUser();
            util.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
            Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(user));
            Mockito.when(userMapper.toDto(user)).thenReturn(new UserResponse());
            var result = authService.me();
            Assertions.assertThat(result).isNotNull();
        }
    }

    private User mockUser() {
        var user = new User();
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        return user;
    }
}
