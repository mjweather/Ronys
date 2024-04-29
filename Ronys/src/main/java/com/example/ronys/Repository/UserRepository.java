package com.example.ronys.Repository;

import com.example.ronys.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByName(String name);
    Users findByName(String name);
}
