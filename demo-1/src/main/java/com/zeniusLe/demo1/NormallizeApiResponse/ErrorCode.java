package com.zeniusLe.demo1.NormallizeApiResponse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
        UNCATEGORIZED_EXCEPTION(9999, "Unauthorized error", HttpStatus.INTERNAL_SERVER_ERROR), // lỗi không xác định
        USER_EXISTED(1001, "User already existed", HttpStatus.BAD_REQUEST),
        INVALID_PASSWORD(1002, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
        USER_NOT_EXISTED(1003, "User not existed", HttpStatus.NOT_FOUND),
        AUTHENTICATED(1004, "Un Authenticated", HttpStatus.UNAUTHORIZED),
        UNAUTHORIZED(1005, "you don't have permission", HttpStatus.FORBIDDEN), // lỗi 403

        ;

        private int code;
        private String message;
        private HttpStatusCode statusCode;

        ErrorCode(int code, String message, HttpStatusCode statusCode) {
            this.code = code;
            this.message = message;
            this.statusCode = statusCode;
        }

//        public int getCode() {
//            return code;
//        }
//
//        public void setCode(int code) {
//            this.code = code;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
}
