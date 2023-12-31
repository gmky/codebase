package gmky.codebase.service;

import gmky.codebase.api.model.ChangePasswordReq;
import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.api.model.RegisterUserReq;
import gmky.codebase.api.model.SummaryResponse;
import gmky.codebase.api.model.UserResponse;

import java.util.List;

public interface AuthService {
    LoginResponse login(LoginReq req);

    UserResponse me();

    List<SummaryResponse> summary();

    void forgotPassword(String email);

    UserResponse register(RegisterUserReq req);

    void changePassword(ChangePasswordReq req);
}
