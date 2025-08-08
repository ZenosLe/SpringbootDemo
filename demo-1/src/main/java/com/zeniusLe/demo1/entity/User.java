package com.zeniusLe.demo1.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
// Annotation này sẽ tự động tạo bảng trong CDSL thông qua Hibernet
// khi đánh dấu @Entity này sẽ tự động coi như class này như 1 bảng trong CSDL
public class User {
    @Id // dùng để đánh dấu khóa chính
    @GeneratedValue(strategy = GenerationType.UUID)

    String id;
    String name;
    String password;
    String email;
    String phone;
    Date birthday;
}
