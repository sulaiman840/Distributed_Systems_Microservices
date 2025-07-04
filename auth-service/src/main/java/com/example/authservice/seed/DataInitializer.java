package com.example.authservice.seed;

import java.util.List;    
import com.example.authservice.model.*;
import com.example.authservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
  private final RoleRepository roleRepo;
  private final UserRepository userRepo;
  private final BCryptPasswordEncoder encoder;

  public DataInitializer(RoleRepository roleRepo,
                         UserRepository userRepo,
                         BCryptPasswordEncoder encoder) {
    this.roleRepo = roleRepo;
    this.userRepo = userRepo;
    this.encoder = encoder;
  }

  @Override
  public void run(String... args) {
    // Create roles if absent
    for (String r : List.of("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT")) {
      roleRepo.findByName(r).orElseGet(() -> {
        Role role = new Role();
        role.setName(r);
        return roleRepo.save(role);
      });
    }

    // Seed default admin
    if (userRepo.findByUsername("admin").isEmpty()) {
      Role adminRole = roleRepo.findByName("ROLE_ADMIN").get();
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword(encoder.encode("admin123"));
      admin.setRole(adminRole);
      userRepo.save(admin);
      System.out.println("Initialized default admin/admin123");
    }
  }
}
