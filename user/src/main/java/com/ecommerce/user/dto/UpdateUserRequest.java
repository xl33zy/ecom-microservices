package com.ecommerce.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain upper, lower, digit and special character"
    )
    private String password;

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ'-]+$", message = "First name contains invalid characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ'-]+$", message = "Last name contains invalid characters")
    private String lastName;

    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must be at most 255 characters")
    private String email;

    @Pattern(regexp = "^\\+[1-9]\\d{9,14}$", message = "Phone must be a valid international number (E.164)")
    private String phone;

    @Valid
    private AddressDTO address;
}
