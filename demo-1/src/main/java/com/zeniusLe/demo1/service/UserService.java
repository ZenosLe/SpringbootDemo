package com.zeniusLe.demo1.service;

import com.zeniusLe.demo1.Mapper.UserMapper;
import com.zeniusLe.demo1.NormallizeApiResponse.ErrorCode;
import com.zeniusLe.demo1.dto.request.UserCreateRequest;
import com.zeniusLe.demo1.dto.request.UserUpdateRequest;
import com.zeniusLe.demo1.dto.response.UserResponse;
import com.zeniusLe.demo1.entity.User;
import com.zeniusLe.demo1.exceptions.AppExceptions;
import com.zeniusLe.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired // Annotations này sẽ đưa ra các @Bean cần thiết
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public User CreateUser(UserCreateRequest request){
        // lấy hàm kiểm tra từ userRepository để check
        if (userRepository.existsByName(request.getName()))
            throw new AppExceptions(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        return userRepository.save(user);
    }


    public List<User> findAll(){
        return userRepository.findAll();
    }

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


}
