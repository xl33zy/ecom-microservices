package com.ecommerce.user.controller;

import com.ecommerce.user.dto.ApiResponseDTO;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.exception.EntityNotFoundException;
import com.ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<UserResponse>>> getAllUsers(WebRequest webRequest) {
        List<UserResponse> users = userService.getAllUsers();
        String requestId = generateRequestId();
        return ResponseEntity.ok(ApiResponseDTO.success(
                users,
                "Users retrieved successfully",
                webRequest.getDescription(false).replace("uri=", ""),
                requestId
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponse>> getUserById(@PathVariable String id, WebRequest webRequest) {
        UserResponse response = userService.getUserById(id)
                                           .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        String requestId = generateRequestId();
        log.info("Request received for user: {}", id);
        return ResponseEntity.ok(ApiResponseDTO.success(
                response,
                "User retrieved successfully",
                webRequest.getDescription(false).replace("uri=", ""),
                requestId
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserResponse>> createUser(@Valid @RequestBody UserRequest userRequest, WebRequest webRequest) {
        UserResponse response = userService.createUser(userRequest);
        String requestId = generateRequestId();
        log.info("Creating new user with email: {}", userRequest.getEmail());
        return ResponseEntity.status(201)
                             .body(ApiResponseDTO.success(response, "User created successfully", webRequest.getDescription(false).replace("uri=", ""), requestId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponse>> updateUser(@PathVariable String id, @Valid @RequestBody UserRequest updateUserRequest, WebRequest webRequest) {
        UserResponse response = userService.updateUser(id, updateUserRequest)
                                           .orElseThrow(() -> new EntityNotFoundException("User not found for update with id: " + id));
        String requestId = generateRequestId();
        log.info("Updating user with id: {}", id);
        return ResponseEntity.ok(ApiResponseDTO.success(
                response,
                "User updated successfully",
                webRequest.getDescription(false).replace("uri=", ""),
                requestId
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable String id, WebRequest webRequest) {
        boolean deleted = userService.deleteUser(id);
        String requestId = generateRequestId();
        if (deleted) {
            log.info("Deleted user with id: {}", id);
            return ResponseEntity.ok(ApiResponseDTO.success(null, "User deleted successfully", webRequest.getDescription(false).replace("uri=", ""), requestId));
        } else {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
    }
}
