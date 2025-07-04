package com.example.authservice.repository;

import com.example.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;    

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  List<User> findByRole_Name(String name);
}
