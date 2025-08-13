package com.zeniusLe.demo1.service.AuthenticationService;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.zeniusLe.demo1.NormallizeApiResponse.ErrorCode;
import com.zeniusLe.demo1.dto.request.AuthenticationRequest.AuthRequest;
import com.zeniusLe.demo1.dto.request.AuthenticationRequest.introspectRequest;
import com.zeniusLe.demo1.dto.response.AuthenticationResponse.AuthResponse;
import com.zeniusLe.demo1.dto.response.AuthenticationResponse.introspectResponse;
import com.zeniusLe.demo1.dto.response.UserResponse;
import com.zeniusLe.demo1.entity.User;
import com.zeniusLe.demo1.exceptions.AppExceptions;
import com.zeniusLe.demo1.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class AuthService {
    UserRepository userRepository;


//https://generate-random.org/encryption-key-generator?count=1&bytes=32&cipher=aes-256-cbc&string=&password=
    @NonFinal
    @Value("${JWT.signerKey }") //@value này dùng để đọc biến từ file yaml
    protected String SIGNER_KEY;


    // dùng để verified token
    public introspectResponse introspect(introspectRequest introRequest)
            throws JOSEException, ParseException {
        var token = introRequest.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return introspectResponse.builder()
                .tokenValid(verified && expityTime.after(new Date()))
                .build();
    }

    //kiểm tra và genarate token cho user đó
    public AuthResponse authenticate(AuthRequest authRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = userRepository.findByUsername(authRequest.getName())
                .orElseThrow(() -> new AppExceptions(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.
                matches(authRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppExceptions(ErrorCode.AUTHENTICATED);
        }

        var token = genarateToken(user);
        return AuthResponse.builder()
                .tokenJWT(token)
                .authenticated(true)
                .build();
    }

    // hàm generate token
    private String genarateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getName())// đại diện cho user name đăng nhập
                .issuer("zeniusLe.com")// xác đinh được token này được cấp phát từ ai
                // thông thường là domain sever
                .issueTime(new Date())// dùng để lấy thời điểm hiện tại
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                )) // dùng để xác đinh thời hạn của token này
                .claim("scope", buildScope(user))
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

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role));
        }
        return stringJoiner.toString();
    }
}
