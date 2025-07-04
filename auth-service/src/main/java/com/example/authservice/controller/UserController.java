package com.example.authservice.controller;

import com.example.authservice.dto.UserResponse;
import com.example.authservice.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserRepository userRepo;

  public UserController(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
   return userRepo.findById(id)
      .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getRole().getName()))
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  } 
}
