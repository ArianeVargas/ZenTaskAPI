package com.zentaskapi.mapper;

import com.zentaskapi.auth.dto.RegisterRequest;
import com.zentaskapi.dto.CreateUserRequest;
import com.zentaskapi.dto.UpdateUserRequest;
import com.zentaskapi.dto.UserResponse;
import com.zentaskapi.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromRegisterRequest(RegisterRequest request);

    UserResponse toDto(User user);

    User fromCreateRequest(CreateUserRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateUserRequest request, @MappingTarget User user);
}