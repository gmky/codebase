package gmky.codebase.web.rest.v1;

import gmky.codebase.api.model.CreateUserReq;
import gmky.codebase.api.model.UpdateUserReq;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.service.UserService;
import gmky.codebase.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import gmky.codebase.api.UserManagementApi;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserResource implements UserManagementApi {
    private final UserService userService;

    @Override
    @PreAuthorize("checkPermission('USER', 'USER_MANAGEMENT', {'create'})")
    public ResponseEntity<UserResponse> createUser(CreateUserReq createUserReq) {
        var result = userService.createUser(createUserReq);
        return ResponseEntity.ok(result);
    }

    @Override
    @PreAuthorize("checkPermission('USER', 'USER_MANAGEMENT', {'view'}) " +
            "|| checkPermission('USER', 'USER_MANAGEMENT', {'edit'}) " +
            "|| checkPermission('USER', 'USER_MANAGEMENT', {'delete'}) " +
            "|| checkPermission('USER', 'USER_MANAGEMENT', {'create'})")
    public ResponseEntity<List<UserResponse>> getAllUsers(String keyword, Pageable pageable) {
        var page = userService.getAllUsers(keyword, pageable);
        var headers = PaginationUtil.generatePaginationHeader(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @Override
    @PreAuthorize("checkPermission('USER', 'USER_MANAGEMENT', {'view'}) || checkPermission('USER', 'USER_MANAGEMENT', {'edit'})")
    public ResponseEntity<UserResponse> getUserDetailById(Long userId) {
        var user = userService.getDetailById(userId);
        return ResponseEntity.ok(user);
    }

    @Override
    @PreAuthorize("checkPermission('USER', 'USER_MANAGEMENT', {'edit'})")
    public ResponseEntity<UserResponse> updateUserById(Long userId, UpdateUserReq updateUserReq) {
        var user = userService.updateUserById(userId, updateUserReq);
        return ResponseEntity.ok(user);
    }
}
