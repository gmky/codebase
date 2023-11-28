package gmky.codebase.service.impl;

import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.api.model.SummaryResponse;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.enumeration.EmailTypeEnum;
import gmky.codebase.exception.ForbiddenException;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.exception.UnauthorizedException;
import gmky.codebase.mapper.FunctionPrivilegeMapper;
import gmky.codebase.mapper.UserMapper;
import gmky.codebase.model.entity.FunctionPrivilege;
import gmky.codebase.model.entity.JobRole;
import gmky.codebase.model.entity.User;
import gmky.codebase.model.event.EnvelopedEvent;
import gmky.codebase.model.event.ForgotPasswordEmailEvent;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.security.TokenProvider;
import gmky.codebase.service.AuthService;
import gmky.codebase.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static gmky.codebase.enumeration.ExceptionEnum.ACCESS_DENIED;
import static gmky.codebase.enumeration.ExceptionEnum.LOGIN_INFO_NOT_MATCH;
import static gmky.codebase.enumeration.ExceptionEnum.USERNAME_NOT_FOUND;
import static gmky.codebase.enumeration.ExceptionEnum.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final FunctionPrivilegeMapper functionPrivilegeMapper;
    private final ApplicationEventPublisher appEventPublisher;

    @Override
    public LoginResponse login(LoginReq req) {
        var user = userRepository.findByUsernameIgnoreCase(req.getUsername())
                .orElseThrow(() -> new NotFoundException(USERNAME_NOT_FOUND));
        var isMatched = passwordEncoder.matches(req.getPassword(), user.getPassword());
        if (!isMatched) throw new UnauthorizedException(LOGIN_INFO_NOT_MATCH);
        var accessToken = tokenProvider.generateToken(user.getUsername(), new HashMap<>());
        var result = new LoginResponse();
        result.setAccessToken(accessToken);
        return result;
    }

    @Override
    public UserResponse me() {
        var currentUsername = SecurityUtil.getCurrentUsername();
        var currentUser = userRepository.findByUsernameIgnoreCase(currentUsername)
                .orElseThrow(() -> new ForbiddenException(ACCESS_DENIED));
        return userMapper.toDto(currentUser);
    }

    @Override
    public List<SummaryResponse> summary() {
        var username = SecurityUtil.getCurrentUsername();
        var user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ForbiddenException(ACCESS_DENIED));
        var allFunctionPrivileges = getAllFunctionPrivileges(user);
        return functionPrivilegeMapper.toSummary(allFunctionPrivileges);
    }

    @Override
    public void forgotPassword(String email) {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        var event = ForgotPasswordEmailEvent.builder()
                .emailType(EmailTypeEnum.FORGOT_PASSWORD)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
        var envelopedEvent = new EnvelopedEvent<>(event);
        appEventPublisher.publishEvent(envelopedEvent);
    }

    private List<FunctionPrivilege> getAllFunctionPrivileges(User user) {
        return user.getJobRoles()
                .stream()
                .filter(this::isNotExpired)
                .map(JobRole::getFunctionPrivileges)
                .flatMap(Collection::stream)
                .toList();
    }

    private boolean isNotExpired(JobRole jobRole) {
        var now = Instant.now();
        return now.isAfter(jobRole.getStartAt()) && now.isBefore(jobRole.getEndAt());
    }
}
