package com.ecommerce.user.service;

import com.ecommerce.user.dto.CreateUserRequest;
import com.ecommerce.user.dto.UpdateUserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.exception.EntityNotFoundException;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.keycloak.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(userMapper::toResponse)
                             .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        String token = keycloakAdminService.getAdminAccessToken();
        String keycloakUserId = keycloakAdminService.createUser(token, request);

        User user = userMapper.toEntity(request);
        user.setKeycloakId(keycloakUserId);

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public UserResponse updateUser(String id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String token = keycloakAdminService.getAdminAccessToken();

        keycloakAdminService.updateUser(token, user.getKeycloakId(), request);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            keycloakAdminService.updatePassword(token, user.getKeycloakId(), request.getPassword());
        }

        userMapper.updateUserFromRequest(request, user);
        User saved = userRepository.save(user);

        return userMapper.toResponse(saved);
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        String token = keycloakAdminService.getAdminAccessToken();

        keycloakAdminService.deleteUser(token, user.getKeycloakId());
        userRepository.delete(user);
    }
}

