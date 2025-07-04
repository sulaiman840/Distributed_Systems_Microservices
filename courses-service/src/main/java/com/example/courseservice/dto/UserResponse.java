package com.example.courseservice.dto;

public record UserResponse(
    Long id,
    String username,
    String role
) { }
