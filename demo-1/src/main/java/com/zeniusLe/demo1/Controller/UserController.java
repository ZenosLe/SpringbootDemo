package com.zeniusLe.demo1.Controller;

import com.zeniusLe.demo1.NormallizeApiResponse.ApiResponse;
import com.zeniusLe.demo1.dto.request.UserCreateRequest;
import com.zeniusLe.demo1.dto.request.UserUpdateRequest;
import com.zeniusLe.demo1.dto.response.UserResponse;
import com.zeniusLe.demo1.entity.User;
import com.zeniusLe.demo1.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest requestUser){
        return ApiResponse.<UserResponse>builder()
                .data(userService.CreateUser(requestUser))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getAllUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("User Name: {}", authentication.getName());
        authentication.getAuthorities().forEach(authority
                -> log.info(authority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{userID}")
    UserResponse findById(@PathVariable("userID") String userID){
        return userService.getUserById(userID);
    }

    @PutMapping("/{userID}")
    UserResponse updateUser(@PathVariable String userID, @RequestBody UserUpdateRequest requestUser){
        return userService.UpdateUser(userID,requestUser);
    }

    @DeleteMapping("/{userID}")
    void deleteById(@PathVariable("userID") String userID){
       userService.DeleteUserById(userID);
    }
}
