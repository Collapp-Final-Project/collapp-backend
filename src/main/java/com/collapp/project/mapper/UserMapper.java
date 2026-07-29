package com.collapp.project.mapper;

import com.collapp.project.dto.user.UserResponse;
import com.collapp.project.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getEmail(),
                user.getSpecialty(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getPortfolioUrl(),
                user.getInstagramUrl()
        );
    }
}