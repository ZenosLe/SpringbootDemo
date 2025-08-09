package com.zeniusLe.demo1.service.AuthenticationService;

import com.zeniusLe.demo1.NormallizeApiResponse.ErrorCode;
import com.zeniusLe.demo1.dto.request.AuthenticationRequest.AuthRequest;
import com.zeniusLe.demo1.dto.response.UserResponse;
import com.zeniusLe.demo1.exceptions.AppExceptions;
import com.zeniusLe.demo1.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthService {
    UserRepository userRepository;

    public boolean authenticate(AuthRequest authRequest) {
        var user = userRepository.findByUsername(authRequest.getName())
                .orElseThrow(() -> new AppExceptions(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(authRequest.getPassword(), user.getPassword());

    }
}
