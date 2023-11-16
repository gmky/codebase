package gmky.codebase.service;

import gmky.codebase.api.model.UpdateUserReq;
import org.springframework.data.domain.Page;
import gmky.codebase.api.model.UserResponse;
import org.springframework.data.domain.Pageable;
import gmky.codebase.api.model.CreateUserReq;

public interface UserService {
    Page<UserResponse> getAllUsers(String keyword, Pageable pageable);

    UserResponse createUser(CreateUserReq req);

    UserResponse getDetailById(Long id);

    UserResponse updateUserById(Long id, UpdateUserReq req);
}
