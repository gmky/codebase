package gmky.codebase.service;

import gmky.codebase.api.model.LoginReq;
import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.api.model.UserResponse;

public interface AuthService {
    LoginResponse login(LoginReq req);

    UserResponse me();
}
