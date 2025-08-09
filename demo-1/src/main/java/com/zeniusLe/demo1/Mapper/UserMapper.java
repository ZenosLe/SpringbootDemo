package com.zeniusLe.demo1.Mapper;

import com.zeniusLe.demo1.dto.request.UserCreateRequest;
import com.zeniusLe.demo1.dto.request.UserUpdateRequest;
import com.zeniusLe.demo1.dto.response.UserResponse;
import com.zeniusLe.demo1.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest userRequest);
    // Method này sẽ nhận 1 parametor userRequest từ UserCreateRequest và trả về class User

    // mapping từ userResponse + User
    UserResponse toUserResponse(User user);

    // update user
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdate);
}
