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
        if (existed) throw new BadRequestException("Username already existed");
        existed = userRepository.existsByEmailIgnoreCase(req.getEmail());
        if (existed) throw new BadRequestException("Email already existed");
        var entity = userMapper.toEntity(req);
        entity.setPassword(passwordEncoder.encode(req.getPassword()));
        entity = userRepository.save(entity);
        return userMapper.toDto(entity);
    }

    @Override
    public UserResponse getDetailById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponse updateUserById(Long id, UpdateUserReq req) {
        var user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        var existed = userRepository.existsByUsernameIgnoreCaseAndIdNot(req.getUsername(), id);
        if (existed) throw new BadRequestException("Username already existed");
        existed = userRepository.existsByEmailIgnoreCaseAndIdNot(req.getEmail(), id);
        if (existed) throw new BadRequestException("Email already existed");
        user = userMapper.partialUpdate(user, req);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
}
