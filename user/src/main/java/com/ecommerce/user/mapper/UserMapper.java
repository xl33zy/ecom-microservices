package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.CreateUserRequest;
import com.ecommerce.user.dto.UpdateUserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "address", source = "address")
    User toEntity(CreateUserRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "keycloakId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "address", source = "address")
    void updateUserFromRequest(UpdateUserRequest request, @MappingTarget User user);

    @Mapping(target = "address", source = "address")
    @Mapping(target = "keycloakId", source = "keycloakId")
    UserResponse toResponse(User user);

    default Address toAddress(AddressDTO dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setZipcode(dto.getZipcode());
        return address;
    }

    default AddressDTO toAddressDTO(Address address) {
        if (address == null) return null;
        AddressDTO dto = new AddressDTO();
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setZipcode(address.getZipcode());
        return dto;
    }
}
