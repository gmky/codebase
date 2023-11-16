package gmky.codebase.service;

import gmky.codebase.api.model.CreateUserReq;
import gmky.codebase.api.model.UpdateUserReq;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.exception.BadRequestException;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.mapper.UserMapper;
import gmky.codebase.model.entity.User;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    private static final String USERNAME = "admin";
    private static final String EMAIL = "admin@gmky.dev";
    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("Get all users should return data")
    void testGetAllUsers_shouldOK() {
        var user = mockUser();
        var userRes = mockUserResponse();
        var pageable = PageRequest.of(0, 10);
        Mockito.when(userRepository.findAllByUsernameStartsWith("admin", pageable)).thenReturn(new PageImpl<>(List.of(user)));
        Mockito.when(userMapper.toDto(user)).thenReturn(userRes);
        var result = userService.getAllUsers("admin", pageable);
        Assertions.assertThat(result).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("Create user should throw BadRequestException when username existed")
    void testCreateUser_shouldThrowBadRequestException_whenUsernameExisted() {
        var req = (new CreateUserReq()).username(USERNAME);
        Mockito.when(userRepository.existsByUsernameIgnoreCase(USERNAME)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> userService.createUser(req))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Create user should throw BadRequestException when email existed")
    void testCreateUser_shouldThrowBadRequestException_whenEmailExisted() {
        var req = (new CreateUserReq()).username(USERNAME).email(EMAIL);
        Mockito.when(userRepository.existsByUsernameIgnoreCase(USERNAME)).thenReturn(false);
        Mockito.when(userRepository.existsByEmailIgnoreCase(EMAIL)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> userService.createUser(req))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Create user should OK")
    void testCreateUser_shouldOK() {
        var req = (new CreateUserReq()).username(USERNAME).email(EMAIL);
        var user = mockUser();
        var userRes = mockUserResponse();
        Mockito.when(userRepository.existsByUsernameIgnoreCase(USERNAME)).thenReturn(false);
        Mockito.when(userRepository.existsByEmailIgnoreCase(EMAIL)).thenReturn(false);
        Mockito.when(userMapper.toEntity(req)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(req.getPassword())).thenReturn("");
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toDto(user)).thenReturn(userRes);
        var result = userService.createUser(req);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Get user detail should throw NotFoundException")
    void testGetUserById_shouldThrowNotFoundException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> userService.getDetailById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Get user detail should OK")
    void testGetUserById_shouldReturnUserDetail() {
        var user = mockUser();
        var userRes = mockUserResponse();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.toDto(user)).thenReturn(userRes);
        var result = userService.getDetailById(1L);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getUsername()).isEqualTo(USERNAME);
        Assertions.assertThat(result.getEmail()).isEqualTo(EMAIL);
    }

    @Test
    @DisplayName("Update user should throw NotFoundException")
    void testUpdateUserById_shouldThrowNotFoundException() {
        var req = new UpdateUserReq();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> userService.updateUserById(1L, req)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Update user should throw BadRequestException when username existed")
    void testUpdateUserById_shouldThrowBadRequestException_whenUsernameExisted() {
        var req = mockUpdateUserReq();
        var user = mockUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsernameIgnoreCaseAndIdNot(USERNAME, 1L)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> userService.updateUserById(1L, req)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Update user should throw BadRequestException when email existed")
    void testUpdateUserById_shouldThrowBadRequestException_whenEmailExisted() {
        var req = mockUpdateUserReq();
        var user = mockUser();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsernameIgnoreCaseAndIdNot(USERNAME, 1L)).thenReturn(false);
        Mockito.when(userRepository.existsByEmailIgnoreCaseAndIdNot(EMAIL, 1L)).thenReturn(true);
        Assertions.assertThatThrownBy(() -> userService.updateUserById(1L, req)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("Update user should OK")
    void testUpdateUserById_shouldOK() {
        var req = mockUpdateUserReq();
        var user = mockUser();
        var userRes = mockUserResponse();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsernameIgnoreCaseAndIdNot(USERNAME, 1L)).thenReturn(false);
        Mockito.when(userRepository.existsByEmailIgnoreCaseAndIdNot(EMAIL, 1L)).thenReturn(false);
        Mockito.when(userMapper.partialUpdate(user, req)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toDto(user)).thenReturn(userRes);
        var result = userService.updateUserById(1L, req);
        Assertions.assertThat(result).isNotNull();
    }

    private User mockUser() {
        var user = new User();
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        return user;
    }

    private UserResponse mockUserResponse() {
        var userRes = new UserResponse();
        userRes.setUsername(USERNAME);
        userRes.setEmail(EMAIL);
        return userRes;
    }

    private UpdateUserReq mockUpdateUserReq() {
        var req = new UpdateUserReq();
        req.setEmail(EMAIL);
        req.setUsername(USERNAME);
        return req;
    }
}
