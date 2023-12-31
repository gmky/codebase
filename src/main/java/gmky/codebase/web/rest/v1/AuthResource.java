package gmky.codebase.web.rest.v1;

import gmky.codebase.api.AuthenticationApi;
import gmky.codebase.api.model.ChangePasswordReq;
import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.api.model.RegisterUserReq;
import gmky.codebase.api.model.SummaryResponse;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthResource implements AuthenticationApi {
    private final AuthService authService;

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordReq changePasswordReq) {
        authService.changePassword(changePasswordReq);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> forgotPassword(String email) {
        authService.forgotPassword(email);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> getCurrentUserInfo() {
        var result = authService.me();
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginReq loginReq) {
        var result = authService.login(loginReq);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<UserResponse> register(RegisterUserReq registerUserReq) {
        var result = authService.register(registerUserReq);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<SummaryResponse>> summary() {
        var result = authService.summary();
        return ResponseEntity.ok(result);
    }
}
