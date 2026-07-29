package com.collapp.project.dto.auth;

public record AuthResponse(
        String token,
        String username,
        String role
) {}