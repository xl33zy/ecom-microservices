package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
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

    @Transactional
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(this::mapToUserResponse)
                             .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse getUserById(String id) {
        return userRepository.findById(id)
                             .map(this::mapToUserResponse)
                             .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        User user = new User();
        updateUserFromRequest(user, userRequest);
        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Transactional
    public UserResponse updateUser(String id, UserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        updateUserFromRequest(user, updateUserRequest);
        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        if (userRequest.getAddress() != null) {
            AddressDTO addrReq = userRequest.getAddress();
            Address address = user.getAddress() != null ? user.getAddress() : new Address();
            address.setStreet(addrReq.getStreet());
            address.setCity(addrReq.getCity());
            address.setState(addrReq.getState());
            address.setCountry(addrReq.getCountry());
            address.setZipcode(addrReq.getZipcode());
            user.setAddress(address);
        }
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if (user.getAddress() != null) {
            Address addr = user.getAddress();
            AddressDTO dto = new AddressDTO();
            dto.setStreet(addr.getStreet());
            dto.setCity(addr.getCity());
            dto.setState(addr.getState());
            dto.setCountry(addr.getCountry());
            dto.setZipcode(addr.getZipcode());
            response.setAddress(dto);
        }
        return response;
    }
}
