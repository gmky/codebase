package gmky.codebase.service.impl;

import gmky.codebase.api.model.CreateUserReq;
import gmky.codebase.api.model.UpdateUserReq;
import gmky.codebase.api.model.UserResponse;
import gmky.codebase.exception.BadRequestException;
import gmky.codebase.exception.NotFoundException;
import gmky.codebase.mapper.UserMapper;
import gmky.codebase.repository.UserRepository;
import gmky.codebase.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gmky.codebase.enumeration.ExceptionEnum.EMAIL_EXISTED;
import static gmky.codebase.enumeration.ExceptionEnum.USERNAME_EXISTED;
import static gmky.codebase.enumeration.ExceptionEnum.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getAllUsers(String keyword, Pageable pageable) {
        var page = userRepository.findAllByUsernameStartsWith(keyword, pageable);
        return page.map(userMapper::toDto);
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserReq req) {
        boolean existed = userRepository.existsByUsernameIgnoreCase(req.getUsername());
        if (existed) throw new BadRequestException(USERNAME_EXISTED);
        existed = userRepository.existsByEmailIgnoreCase(req.getEmail());
        if (existed) throw new BadRequestException(EMAIL_EXISTED);
        var entity = userMapper.toEntity(req);
        entity.setPassword(passwordEncoder.encode(req.getPassword()));
        entity = userRepository.save(entity);
        return userMapper.toDto(entity);
    }

    @Override
    public UserResponse getDetailById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponse updateUserById(Long id, UpdateUserReq req) {
        var user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        var existed = userRepository.existsByUsernameIgnoreCaseAndIdNot(req.getUsername(), id);
        if (existed) throw new BadRequestException(USERNAME_EXISTED);
        existed = userRepository.existsByEmailIgnoreCaseAndIdNot(req.getEmail(), id);
        if (existed) throw new BadRequestException(EMAIL_EXISTED);
        user = userMapper.partialUpdate(user, req);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
}
