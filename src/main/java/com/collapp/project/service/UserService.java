package com.collapp.project.service;

import com.collapp.project.dto.user.UserResponse;
import com.collapp.project.dto.user.UserUpdateRequest;

public interface UserService {
    UserResponse getCurrentUser(String email);
    UserResponse updateCurrentUser(String email, UserUpdateRequest request);
}