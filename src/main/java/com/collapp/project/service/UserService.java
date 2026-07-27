package com.collapp.project.service;

import com.collapp.project.dto.user.UserResponse;

public interface UserService {
    UserResponse getCurrentUser(String email);
}