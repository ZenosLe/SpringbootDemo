package com.zeniusLe.demo1.Controller;

import com.zeniusLe.demo1.NormallizeApiResponse.ApiResponse;
import com.zeniusLe.demo1.dto.request.UserCreateRequest;
import com.zeniusLe.demo1.dto.request.UserUpdateRequest;
import com.zeniusLe.demo1.entity.User;
import com.zeniusLe.demo1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreateRequest requestUser){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.CreateUser(requestUser));
        return apiResponse;
    }

    @GetMapping
    List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{userID}")
    User findById(@PathVariable("userID") String userID){
        return userService.getUserById(userID);
    }

    @PutMapping("/{userID}")
    User updateUser(@PathVariable String userID, @RequestBody UserUpdateRequest requestUser){
        return userService.UpdateUser(userID,requestUser);
    }

    @DeleteMapping("/{userID}")
    void deleteById(@PathVariable("userID") String userID){
       userService.DeleteUserById(userID);
    }
}
