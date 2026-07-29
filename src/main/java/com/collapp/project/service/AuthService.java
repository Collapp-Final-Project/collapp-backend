package com.collapp.project.service;

import com.collapp.project.dto.auth.AuthResponse;
import com.collapp.project.dto.auth.LoginRequest;
import com.collapp.project.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}