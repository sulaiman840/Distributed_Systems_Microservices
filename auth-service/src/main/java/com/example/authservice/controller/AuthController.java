package com.example.authservice.controller;

import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.RegisterRequest;
import com.example.authservice.dto.RoleRequest;       // ← new
import com.example.authservice.dto.UserResponse;   
import com.example.authservice.model.Role;
import com.example.authservice.model.User;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;                    // ← new
import java.util.stream.Collectors; 

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authManager;
  private final JwtUtil jwtUtil;
  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final BCryptPasswordEncoder passwordEncoder;

  public AuthController(AuthenticationManager authManager,
                        JwtUtil jwtUtil,
                        UserRepository userRepo,
                        RoleRepository roleRepo,
                        BCryptPasswordEncoder passwordEncoder) {
    this.authManager    = authManager;
    this.jwtUtil        = jwtUtil;
    this.userRepo       = userRepo;
    this.roleRepo       = roleRepo;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(
      @RequestBody RegisterRequest dto,
      Authentication auth               // spring injects the caller’s auth if present
  ) {
    String requestedRole = dto.role().toUpperCase();

    // 1) No one can self-register as ADMIN
    if ("ADMIN".equals(requestedRole)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Registration of ADMIN users is not allowed");
    }

    // 2) Only existing ADMINs can register TEACHERs
    if ("TEACHER".equals(requestedRole)) {
      boolean callerIsAdmin = auth != null &&
        auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
      if (!callerIsAdmin) {
        return ResponseEntity
          .status(HttpStatus.FORBIDDEN)
          .body("Only admins can register teachers");
      }
    }

    // 3) STUDENT or any other role passes through (no token required)

    // 4) Check duplicated username
    if (userRepo.findByUsername(dto.username()).isPresent()) {
      return ResponseEntity
        .badRequest()
        .body("Username already exists");
    }

    // 5) Persist new user
    Role role = roleRepo.findByName("ROLE_" + requestedRole)
        .orElseThrow(() -> new IllegalArgumentException("Role not found"));
    User user = new User();
    user.setUsername(dto.username());
    user.setPassword(passwordEncoder.encode(dto.password()));
    user.setRole(role);
    userRepo.save(user);

    return ResponseEntity.ok("Registered new " + requestedRole.toLowerCase());
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest dto) {
    // authenticate or throw
    authManager.authenticate(
      new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
    );

    // load user & issue JWT
    User user = userRepo.findByUsername(dto.username())
        .orElseThrow(() -> new RuntimeException("User not found"));
    String token = jwtUtil.generateToken(
      user.getUsername(),
      user.getRole().getName() , 
      user.getId()
    );
    return ResponseEntity.ok(new AuthResponse(token));
  }
  @PostMapping("/by-role")
  public ResponseEntity<List<UserResponse>> getUsersByRole(
      @RequestBody RoleRequest request
  ) {
    // prefix with ROLE_ to match how you store names
    String fullRoleName = "ROLE_" + request.role().toUpperCase();

    List<User> users = userRepo.findByRole_Name(fullRoleName);

    List<UserResponse> response = users.stream()
      .map(u -> new UserResponse(
          u.getId(),
          u.getUsername(),
          u.getRole().getName()
        ))
      .collect(Collectors.toList());

    return ResponseEntity.ok(response);
  }
}
