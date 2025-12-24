package com.ecommerce.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressDTO {

    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street must be at most 255 characters")
    private String street;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be at most 100 characters")
    private String city;

    @Size(max = 100, message = "State must be at most 100 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be at most 100 characters")
    private String country;

    @NotBlank(message = "Zipcode is required")
    @Size(max = 20, message = "Zipcode must be at most 20 characters")
    private String zipcode;
}
