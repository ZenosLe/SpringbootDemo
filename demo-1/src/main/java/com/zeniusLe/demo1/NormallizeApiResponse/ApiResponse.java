package com.zeniusLe.demo1.NormallizeApiResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ApiResponse <T>{
    // lớp này dùng để chuẩn hóa các lỗi khi Response
    // chứa tất cả các lỗi từ 101,102....
    // <T> : thay đổi tùy thuộc từng API
     int code = 1000;
     String message;
     T data;

}

