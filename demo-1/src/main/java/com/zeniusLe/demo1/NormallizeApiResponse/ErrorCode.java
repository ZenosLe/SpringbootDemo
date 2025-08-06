package com.zeniusLe.demo1.NormallizeApiResponse;

public enum ErrorCode {
        USER_EXISTED(1001, "User already existed"),
        INVALID_PASSWORD(1002, "Password must be at least 8 characters"),
        ;

        private int code;
        private String message;

        ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
}
