package gmky.codebase.service.impl;

import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.exception.UnauthorizedException;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.security.TokenProvider;
import gmky.codebase.service.AuthService;
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

    @Override
    public LoginResponse login(LoginReq req) {
        log.info("Login with username: [{}]", req.getUsername());
        var user = userRepository.findByUsernameIgnoreCase(req.getUsername())
                .orElseThrow(() -> new NotFoundException("Username not found"));
        var isMatched = passwordEncoder.matches(req.getPassword(), user.getPassword());
        if (!isMatched) throw new UnauthorizedException("Username and password not match");
        var accessToken = tokenProvider.generateToken(user.getUsername(), new HashMap<>());
        var result = new LoginResponse();
        result.setAccessToken(accessToken);
        return result;
    }
}