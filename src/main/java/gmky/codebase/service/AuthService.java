package gmky.codebase.service;

import gmky.codebase.api.model.LoginResponse;
import gmky.codebase.api.model.LoginReq;

public interface AuthService {
    LoginResponse login(LoginReq req);
}
