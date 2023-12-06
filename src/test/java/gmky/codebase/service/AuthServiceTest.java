package gmky.codebase.service;

import gmky.codebase.api.model.ChangePasswordReq;
import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.RegisterUserReq;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.enumeration.UserStatusEnum;
import gmky.codebase.exception.BadRequestException;
import gmky.codebase.exception.ForbiddenException;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.exception.UnauthorizedException;
import gmky.codebase.mapper.FunctionPrivilegeMapper;
import gmky.codebase.mapper.UserMapper;
import gmky.codebase.model.entity.JobRole;
import gmky.codebase.model.entity.User;
import gmky.codebase.model.event.EnvelopedEvent;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.security.TokenProvider;
import gmky.codebase.service.impl.AuthServiceImpl;
import gmky.codebase.util.SecurityUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private static final String USERNAME = "admin1234";
    private static final String RAW_PASSWORD = "b15dcpt082";
    private static final String EMAIL = "admin@gmky.dev";
    private static final String FULL_NAME = "Vu Hoang Hiep";
    @Mock
    UserMapper userMapper;

    @Mock
    UserRepository userRepository;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    FunctionPrivilegeMapper functionPrivilegeMapper;

    @Mock
    ApplicationEventPublisher appEventPublisher;

    @InjectMocks
    AuthServiceImpl authService;

    @Test
    @DisplayName("Login should OK")
    void login_shouldReturnToken() {
        var user = mockUser(UserStatusEnum.ACTIVE);
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
        var user = mockUser(UserStatusEnum.ACTIVE);
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
            var user = mockUser(UserStatusEnum.ACTIVE);
            util.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
            Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(user));
            Mockito.when(userMapper.toDto(user)).thenReturn(new UserResponse());
            var result = authService.me();
            Assertions.assertThat(result).isNotNull();
        }
    }

    @Test
    @DisplayName("Get summary should return data")
    void testSummary_shouldOK() {
        try (MockedStatic<SecurityUtil> util = Mockito.mockStatic(SecurityUtil.class)) {
            var user = mockUser(UserStatusEnum.ACTIVE);
            util.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
            Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(user));
            Mockito.when(functionPrivilegeMapper.toSummary(Mockito.any())).thenReturn(List.of());
            var result = authService.summary();
            Assertions.assertThat(result).isNotNull();
        }
    }

    @Test
    @DisplayName("Get summary should throw AccessDeniedException")
    void testSummary_shouldThrowAccessDeniedException() {
        try (MockedStatic<SecurityUtil> util = Mockito.mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
            Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());
            Assertions.assertThatThrownBy(() -> authService.summary()).isInstanceOf(ForbiddenException.class);
        }
    }

    @ParameterizedTest
    @EnumSource(value = UserStatusEnum.class, names = {"PENDING", "IN_ACTIVE"})
    @DisplayName("Forgot password should throw ForbiddenException when status is not active")
    void testForgotPassword_shouldThrowForbiddenException_whenStatusIsNotActive(UserStatusEnum status) {
        Mockito.when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(mockUser(status)));
        Assertions.assertThatThrownBy(() -> authService.forgotPassword(EMAIL)).isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("Forgot password should throw NotFoundException")
    void testForgotPassword_shouldThrowNotFoundException() {
        Mockito.when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> authService.forgotPassword(EMAIL)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Forgot password should OK")
    void testForgotPassword_shouldOK() {
        Mockito.when(userRepository.findByEmailIgnoreCase(EMAIL)).thenReturn(Optional.of(mockUser(UserStatusEnum.ACTIVE)));
        Mockito.doNothing().when(appEventPublisher).publishEvent(Mockito.any(EnvelopedEvent.class));
        authService.forgotPassword(EMAIL);
        Mockito.verify(appEventPublisher, Mockito.times(1)).publishEvent(Mockito.any(EnvelopedEvent.class));
    }

    @Test
    @DisplayName("Register should throw BadRequest when username existed")
    void testRegister_shouldThrowBadRequestException_whenUsernameExisted() {
        var req = mockRegisterReq();
        Mockito.when(userRepository.existsByUsernameIgnoreCase(USERNAME)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> authService.register(req)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Register should throw BadRequest when username existed")
    void testRegister_shouldThrowBadRequestException_whenEmailExisted() {
        var req = mockRegisterReq();
        Mockito.when(userRepository.existsByEmailIgnoreCase(EMAIL)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> authService.register(req)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Register should OK")
    void testRegister_shouldOK() {
        var req = mockRegisterReq();
        var user = mockUser(UserStatusEnum.ACTIVE);
        var res = mockUserResponse();
        Mockito.when(userRepository.existsByUsernameIgnoreCase(USERNAME)).thenReturn(false);
        Mockito.when(userRepository.existsByEmailIgnoreCase(EMAIL)).thenReturn(false);
        Mockito.when(userMapper.toEntity(req)).thenReturn(user);
        Mockito.when(userMapper.toDto(user)).thenReturn(res);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.doNothing().when(appEventPublisher).publishEvent(Mockito.any(EnvelopedEvent.class));
        var result = authService.register(req);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    @DisplayName("Change password should throw BadRequestException when password is the same")
    void testChangePassword_shouldThrowBadRequestException_whenPasswordIsTheSame() {
        var reqBody = (new ChangePasswordReq()).newPassword("b15dcpt082").currentPassword("b15dcpt082");
        Assertions.assertThatThrownBy(() -> authService.changePassword(reqBody)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Change password should throw NotFoundException if user not found")
    void testChangePassword_shouldThrowBadRequestException_whenUserNotFound() {
        var reqBody = (new ChangePasswordReq()).newPassword("b15dcpt082").currentPassword("b15dcpt0822");
        try (MockedStatic<SecurityUtil> util = Mockito.mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
            Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());
            Assertions.assertThatThrownBy(() -> authService.changePassword(reqBody)).isInstanceOf(NotFoundException.class);
        }
    }

    @Test
    @DisplayName("Change password should OK")
    void testChangePassword_shouldOK() {
        var reqBody = (new ChangePasswordReq()).newPassword("b15dcpt082").currentPassword("b15dcpt0822");
        var user = mockUser(UserStatusEnum.ACTIVE);
        try (MockedStatic<SecurityUtil> util = Mockito.mockStatic(SecurityUtil.class)) {
            util.when(SecurityUtil::getCurrentUsername).thenReturn(USERNAME);
            Mockito.when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(user));
            Mockito.when(passwordEncoder.matches(Mockito.any(), Mockito.any())).thenReturn(true);
            Mockito.when(passwordEncoder.encode(reqBody.getNewPassword())).thenReturn("hashedPassword");
            Mockito.when(userRepository.save(user)).thenReturn(user);
            authService.changePassword(reqBody);
            Mockito.verify(userRepository, Mockito.times(1)).save(user);
        }
    }

    private User mockUser(UserStatusEnum status) {
        var user = new User();
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        user.setFullName(FULL_NAME);
        user.setStatus(status);
        user.setJobRoles(Set.of(mockJobRole()));
        return user;
    }

    private JobRole mockJobRole() {
        var jobRole = new JobRole();
        jobRole.setStartAt(Instant.now().minusSeconds(5000));
        jobRole.setEndAt(Instant.now().plus(50, ChronoUnit.MINUTES));
        jobRole.setFunctionPrivileges(Set.of());
        return jobRole;
    }

    private RegisterUserReq mockRegisterReq() {
        var req = new RegisterUserReq();
        req.setUsername(USERNAME);
        req.setEmail(EMAIL);
        req.setPassword("b15dcpt082");
        return req;
    }

    private UserResponse mockUserResponse() {
        var userRes = new UserResponse();
        userRes.setUsername(USERNAME);
        userRes.setEmail(EMAIL);
        return userRes;
    }
}
