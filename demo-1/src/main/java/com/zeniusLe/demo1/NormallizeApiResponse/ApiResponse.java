package com.zeniusLe.demo1.NormallizeApiResponse;

public class ApiResponse <T>{
    // lớp này dùng để chuẩn hóa các lỗi khi Response
    // chứa tất cả các lỗi từ 101,102....
    // <T> : thay đổi tùy thuộc từng API
    private int code = 1000;
    private String message;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

