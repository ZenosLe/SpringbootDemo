package com.zeniusLe.demo1.Controller.Authentication;

import com.nimbusds.jose.JOSEException;
import com.zeniusLe.demo1.NormallizeApiResponse.ApiResponse;
import com.zeniusLe.demo1.dto.request.AuthenticationRequest.AuthRequest;
import com.zeniusLe.demo1.dto.request.AuthenticationRequest.introspectRequest;
import com.zeniusLe.demo1.dto.response.AuthenticationResponse.AuthResponse;
import com.zeniusLe.demo1.dto.response.AuthenticationResponse.introspectResponse;
import com.zeniusLe.demo1.service.AuthenticationService.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthController {
    AuthService authService;
//    @PostMapping("/token")
            // cũ (không sử dụng JWT)
//    ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest authRequest){
//        boolean res = authService.authenticate(authRequest);
//        return ApiResponse.<AuthResponse>builder()
//                .data(AuthResponse.builder()
//                        .authenticated(res)
//                        .build())
//                .build();
//    }
            // Update (Sử dụng JWT)
    @PostMapping("/token")
    ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest authRequest){
        var res = authService.authenticate(authRequest);
        return ApiResponse.<AuthResponse>builder()
                .data(res)
                .build();
    }

    // verified token
    @PostMapping("/introspect")
    ApiResponse<introspectResponse> authenticate(@RequestBody introspectRequest introRequest)
            throws ParseException, JOSEException {
        var res = authService.introspect(introRequest);
        return ApiResponse.<introspectResponse>builder()
                .data(res)
                .build();
    }
}
