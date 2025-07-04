package com.example.authservice.dto;

public record UserResponse(
    Long id,
    String username,
    String role
) { }
