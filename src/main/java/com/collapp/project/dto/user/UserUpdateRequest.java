package com.collapp.project.dto.user;

public record UserUpdateRequest(
        String fullName,
        String bio,
        String avatarUrl,
        String portfolioUrl,
        String instagramUrl
) {}
