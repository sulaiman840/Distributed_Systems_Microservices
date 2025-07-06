package com.example.courseservice.controller;

import com.example.courseservice.dto.*;
import com.example.courseservice.model.Course;
import com.example.courseservice.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {

  private final CourseService service;

  public CourseController(CourseService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<?> create(
      @RequestBody CourseRequest req,
      Authentication auth     
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();              
    String role = jwt.getClaimAsString("role");       
  
    if (!"ROLE_TEACHER".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only teachers can create courses");
    }

    Long teacherId = jwt.getClaim("userId");
    Course c = new Course();
    c.setTeacherId(teacherId);
    c.setTitle(req.title());
    c.setDescription(req.description());
    c.setPrice(req.price());
    Course saved = service.create(c);
    return ResponseEntity.ok(toDto(saved));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(
      @PathVariable Long id,
      @RequestBody CourseRequest req,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();              
    String role = jwt.getClaimAsString("role");        
  
    if (!"ROLE_TEACHER".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only teachers can create courses");
    }
    Course existing = service.get(id);
    if (!existing.getTeacherId().equals(jwt.getClaim("userId"))) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot update others’ courses");
    }
    existing.setTitle(req.title());
    existing.setDescription(req.description());
      existing.setPrice(req.price()); 
    return ResponseEntity.ok(toDto(service.update(id, existing)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(
      @PathVariable Long id,
      Authentication auth
  ) {
    Jwt jwt = (Jwt) auth.getPrincipal();               
    String role = jwt.getClaimAsString("role");      
  
    if (!"ROLE_TEACHER".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only teachers can create courses");
    }
    Course existing = service.get(id);
    if (!existing.getTeacherId().equals(jwt.getClaim("userId"))) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot delete others’ courses");
    }
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public List<CourseResponse> list() {
    return service.list().stream()
    .filter(Course::isApproved)
    .map(this::toDto)
    .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CourseResponse> get(@PathVariable Long id) {
    return ResponseEntity.ok(toDto(service.get(id)));
  }

  @GetMapping("/my")
  public List<CourseResponse> myCourses(Authentication auth) {
    Jwt jwt = (Jwt) auth.getPrincipal();
    Long teacherId = jwt.getClaim("userId");
    return service.forTeacher(teacherId)
                  .stream()
                  .map(this::toDto)
                  .collect(Collectors.toList());
  }

  @PutMapping("/{id}/approve")
  public ResponseEntity<?> approveCourse(
      @PathVariable Long id,
      Authentication auth
  ) {

    Jwt jwt = (Jwt) auth.getPrincipal();                  
    String role = jwt.getClaimAsString("role");        
    if (!"ROLE_ADMIN".equals(role)) {
      return ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body("Only admins can approve courses");
    }
 
    Course approved = service.approve(id);
    return ResponseEntity.ok(toDto(approved));
  }
  private CourseResponse toDto(Course c) {
    return new CourseResponse(c.getId(), c.getTeacherId(),
                              c.getTitle(), c.getDescription()    , c.getPrice()   , c.isApproved());
  }
}
