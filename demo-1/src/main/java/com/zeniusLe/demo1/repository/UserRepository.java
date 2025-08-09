package com.zeniusLe.demo1.repository;

import com.zeniusLe.demo1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// lớp này dùng để giao tiếp với database
public interface UserRepository extends JpaRepository<User,String> {
    // kiểm tra ở CSDL có user nào bị trùng name không
    boolean existsByName(String name);
}
