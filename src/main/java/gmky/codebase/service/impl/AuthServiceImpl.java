package gmky.codebase.service.impl;

import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.exception.ForbiddenException;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.exception.UnauthorizedException;
import gmky.codebase.mapper.UserMapper;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.security.TokenProvider;
import gmky.codebase.service.AuthService;
import gmky.codebase.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public LoginResponse login(LoginReq req) {
        var user = userRepository.findByUsernameIgnoreCase(req.getUsername())
                .orElseThrow(() -> new NotFoundException("Username not found"));
        var isMatched = passwordEncoder.matches(req.getPassword(), user.getPassword());
        if (!isMatched) throw new UnauthorizedException("Username and password not match");
        var accessToken = tokenProvider.generateToken(user.getUsername(), new HashMap<>());
        var result = new LoginResponse();
        result.setAccessToken(accessToken);
        return result;
    }

    @Override
    public UserResponse me() {
        var currentUsername = SecurityUtil.getCurrentUsername();
        var currentUser = userRepository.findByUsernameIgnoreCase(currentUsername)
                .orElseThrow(() -> new ForbiddenException("Access Denied"));
        return userMapper.toDto(currentUser);
    }
}
