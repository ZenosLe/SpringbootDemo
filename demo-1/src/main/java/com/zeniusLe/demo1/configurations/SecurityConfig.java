package com.zeniusLe.demo1.configurations;

import com.zeniusLe.demo1.enums.role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // đây là danh sách chứa các endpoint
    private final String[] PUBLIC_END_POINTS = {"/user",
                        "/auth/token", "/auth/introspect"};

    @Value("${JWT.signerKey}")
    private String JWT_SIGNER_KEY;

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() { // dùng để Custom từ SCOPE -> ROLE
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecutiry)
            throws Exception {
        httpSecutiry.authorizeHttpRequests(req ->
                //public các endpoint này mà không cần có sự allow
                req.requestMatchers(HttpMethod.POST, PUBLIC_END_POINTS).permitAll()
                        // những endpoint user nào có SCOPE_ADMIN thì mới get được users
                        .requestMatchers(HttpMethod.GET, "/user")
                        .hasAuthority(role.ADMIN.name())
                            .anyRequest().authenticated()); // tất cả các endpoint khác cần phải có sự allow, có token

        // cung cấp JWT token
        httpSecutiry.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer
                        -> jwtConfigurer.decoder(jwtDecoder())// sử dụng JwtDecoder để kiểm tra
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )

        );

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

    @Bean
    PasswordEncoder passwordEncoder() { // tạo ra password encoder để sử dụng cho tất cả các trường hợp sau này
        return new BCryptPasswordEncoder(10);
    }
}
