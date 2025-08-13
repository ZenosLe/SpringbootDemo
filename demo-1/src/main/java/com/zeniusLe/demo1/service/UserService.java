package com.zeniusLe.demo1.service;

import com.zeniusLe.demo1.Mapper.UserMapper;
import com.zeniusLe.demo1.NormallizeApiResponse.ErrorCode;
import com.zeniusLe.demo1.dto.request.UserCreateRequest;
import com.zeniusLe.demo1.dto.request.UserUpdateRequest;
import com.zeniusLe.demo1.dto.response.UserResponse;
import com.zeniusLe.demo1.enums.role;
import com.zeniusLe.demo1.entity.User;
import com.zeniusLe.demo1.exceptions.AppExceptions;
import com.zeniusLe.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired // Annotations này sẽ đưa ra các @Bean cần thiết
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public UserResponse CreateUser(UserCreateRequest request){
        // lấy hàm kiểm tra từ userRepository để check
        if (userRepository.existsByName(request.getName()))
            throw new AppExceptions(ErrorCode.USER_EXISTED);
        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(role.USER.name()); // mặc đinh khi tạo tài khoản thì sẽ là user

        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')") // kiểm tra role trước sau đó mới gọi method
    // PreAuthorize thường được dùng để kiểm tra ROLE
    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream().map(user ->
                userMapper.toUserResponse(user)).collect(Collectors.toList());
    }

    @PostAuthorize("returnObject.name ==     authentication.name") // gọi method trước sau đó mới kiểm tra role
    // PostAuthorize thường được dùng để lấy thông tin của chính user đó
    public UserResponse getUserById(String id){
        return userMapper.toUserResponse(userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Not found User")));
    }

    public UserResponse UpdateUser(String id, UserUpdateRequest userupdate){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found User"));
        userMapper.updateUser(user, userupdate);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void DeleteUserById(String id){
        userRepository.deleteById(id);
    }

    public UserResponse getMyInfor(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(()
                -> new AppExceptions(ErrorCode.USER_NOT_EXISTED));

        return  userMapper.toUserResponse(user);
    }
}
