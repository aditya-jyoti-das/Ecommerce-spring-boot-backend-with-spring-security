package com.aditya.Ecommerce_java_spring_boot_backend.Repository;

import com.aditya.Ecommerce_java_spring_boot_backend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByUsername(String username);
}
