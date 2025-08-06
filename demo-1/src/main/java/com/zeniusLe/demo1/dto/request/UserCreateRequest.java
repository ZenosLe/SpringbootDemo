package com.zeniusLe.demo1.dto.request;

import jakarta.validation.constraints.Size;

import java.util.Date;

public class UserCreateRequest {
    private String name;
                // message này phải giống biến Enum trong ErrorCode.java
    @Size(min = 8, message = "INVALID_PASSWORD") // đây là validation
    private String password;
    private String email;
    private String phone;
    private Date birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
