package com.zeniusLe.demo1.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // đây là danh sách chứa các endpoint
    private final String[] PUBLIC_END_POINTS = {"/users",
                        "/auth/token", "/auth/introspect"};

    @Value("${JWT.signerKey}")
    private String JWT_SIGNER_KEY;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecutiry)
            throws Exception {
        httpSecutiry.authorizeHttpRequests(req ->
                req.requestMatchers(HttpMethod.POST, PUBLIC_END_POINTS).permitAll() // tại dòng này ta sẽ public endpoint này mà không cần có sự allow
                        .anyRequest().authenticated()); // tất cả các endpoint khác cần phải có sự allow, có token

        // cung cấp JWT token
        httpSecutiry.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));// sử dụng JwtDecoder để kiểm tra

        httpSecutiry.csrf(AbstractHttpConfigurer::disable);

        return httpSecutiry.build();
    }

    // Giải mã & kiểm tra JWT Token bằng JwtDecoder
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(JWT_SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
