package com.collapp.project.dto.user;

import com.collapp.project.entity.enums.Specialty;

public record UserResponse(
        Long id,
        String fullName,
        String username,
        String email,
        Specialty specialty,
        String bio,
        String avatarUrl,
        String portfolioUrl,
        String instagramUrl
) {}