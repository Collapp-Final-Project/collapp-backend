package com.collapp.project.service.impl;

import com.collapp.project.dto.auth.AuthResponse;
import com.collapp.project.dto.auth.LoginRequest;
import com.collapp.project.dto.auth.RegisterRequest;
import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.SystemRole;
import com.collapp.project.repository.UserRepository;
import com.collapp.project.security.CustomUserDetails;
import com.collapp.project.security.JwtService;
import com.collapp.project.service.AuthService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new EntityExistsException("El username ya está en uso: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new EntityExistsException("El email ya está registrado: " + request.email());
        }

        User user = User.builder()
                .username(request.username())
                .fullName(request.fullName())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .specialty(request.specialty())
                .systemRole(SystemRole.ROLE_CREATIVE)
                .avatarUrl(request.avatarUrl())
                .portfolioUrl(request.portfolioUrl())
                .instagramUrl(request.instagramUrl())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(new CustomUserDetails(user));

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getSystemRole().name()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getUsername(),
                user.getSystemRole().name()
        );
    }
}