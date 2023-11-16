package gmky.codebase.web.rest.v1;

import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import gmky.codebase.api.AuthenticationApi;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthResource implements AuthenticationApi {
    private final AuthService authService;
    @Override
    public ResponseEntity<LoginResponse> login(LoginReq loginReq) {
        log.info("Login with username: [{}]", loginReq.getUsername());
        var result = authService.login(loginReq);
        return ResponseEntity.ok(result);
    }
}
