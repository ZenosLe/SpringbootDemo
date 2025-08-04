package com.zeniusLe.demo1.service;

import com.zeniusLe.demo1.dto.request.UserCreateRequest;
import com.zeniusLe.demo1.dto.request.UserUpdateRequest;
import com.zeniusLe.demo1.entity.User;
import com.zeniusLe.demo1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired // Annotations này sẽ đưa ra các @Bean cần thiết
    private UserRepository userRepository;

    public User CreateUser(UserCreateRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setBirthday(request.getBirthday());

        return userRepository.save(user);
    }


    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User getUserById(String id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found User"));
    }

    public User UpdateUser(String id, UserUpdateRequest userupdate){
        User user = getUserById(id);
        user.setName(userupdate.getName());
        user.setPassword(userupdate.getPassword());
        user.setEmail(userupdate.getEmail());
        user.setPhone(userupdate.getPhone());
        user.setBirthday(userupdate.getBirthday());

        return userRepository.save(user);
    }

    public void DeleteUserById(String id){
        userRepository.deleteById(id);
    }


}
