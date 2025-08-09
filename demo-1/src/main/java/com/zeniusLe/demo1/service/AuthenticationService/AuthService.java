package com.zeniusLe.demo1.service.AuthenticationService;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.zeniusLe.demo1.NormallizeApiResponse.ErrorCode;
import com.zeniusLe.demo1.dto.request.AuthenticationRequest.AuthRequest;
import com.zeniusLe.demo1.dto.response.AuthenticationResponse.AuthResponse;
import com.zeniusLe.demo1.dto.response.UserResponse;
import com.zeniusLe.demo1.exceptions.AppExceptions;
import com.zeniusLe.demo1.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthService {
    UserRepository userRepository;


//https://generate-random.org/encryption-key-generator?count=1&bytes=32&cipher=aes-256-cbc&string=&password=
    @NonFinal
    protected static final String SIGNER_KEY =
            "I91AUqMKCNJHx2TbtPcMljN70rpXrx7DLIhGEPbQ9kTyBM5Mz3SEgSLw6oD5YW3t";

    public AuthResponse authenticate(AuthRequest authRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = userRepository.findByUsername(authRequest.getName())
                .orElseThrow(() -> new AppExceptions(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.
                matches(authRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppExceptions(ErrorCode.AUTHENTICATED);
        }

        var token = genarateToken(authRequest.getName());
        return AuthResponse.builder()
                .tokenJWT(token)
                .authenticated(true)
                .build();
    }

    private String genarateToken(String username) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)// đại diện cho user đăng nhập
                .issuer("zeniusLe.com")// xác đinh được token này được cấp phát từ ai
                // thông thường là domain sever
                .issueTime(new Date())// dùng để lấy thời điểm hiện tại
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                )) // dùng để xác đinh thời hạn của token này
                .build();
        Payload payLoad = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject  jwsObject = new JWSObject(header,payLoad);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cant create token", e);
            throw new RuntimeException(e);
        }
    }
}
