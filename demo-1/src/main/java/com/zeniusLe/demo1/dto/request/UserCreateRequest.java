package com.zeniusLe.demo1.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserCreateRequest {
    String name;
                // message này phải giống biến Enum trong ErrorCode.java
    @Size(min = 8, message = "INVALID_PASSWORD") // đây là validation
    String password;
    String email;
    String phone;
    Date birthday;
}
