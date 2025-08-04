package com.zeniusLe.demo1.entity;
import jakarta.persistence.*;

import java.util.Date;

@Entity
// Annotation này sẽ tự động tạo bảng trong CDSL thông qua Hibernet
// khi đánh dấu @Entity này sẽ tự động coi như class này như 1 bảng trong CSDL
public class User {
    @Id // dùng để đánh dấu khóa chính
    @GeneratedValue(strategy = GenerationType.UUID)

    private String id;
    private String name;
    private String password;
    private String email;
    private String phone;
    private Date birthday;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
