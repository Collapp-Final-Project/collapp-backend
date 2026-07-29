package com.collapp.project.service.impl;

import com.collapp.project.dto.user.UserResponse;
import com.collapp.project.dto.user.UserUpdateRequest;
import com.collapp.project.entity.User;
import com.collapp.project.exception.ResourceNotFoundException;
import com.collapp.project.mapper.UserMapper;
import com.collapp.project.repository.UserRepository;
import com.collapp.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateCurrentUser(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.fullName() != null) {
            user.setFullName(request.fullName());
        }
        if (request.bio() != null) {
            user.setBio(request.bio());
        }
        if (request.avatarUrl() != null) {
            user.setAvatarUrl(request.avatarUrl());
        }
        if (request.portfolioUrl() != null) {
            user.setPortfolioUrl(request.portfolioUrl());
        }
        if (request.instagramUrl() != null) {
            user.setInstagramUrl(request.instagramUrl());
        }

        userRepository.save(user);
        return userMapper.toResponse(user);
    }
}