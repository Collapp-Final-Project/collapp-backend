package com.collapp.project.service.impl;

import com.collapp.project.dto.auth.AuthResponse;
import com.collapp.project.dto.auth.LoginRequest;
import com.collapp.project.dto.auth.RegisterRequest;
import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.SystemRole;
import com.collapp.project.repository.UserRepository;
import com.collapp.project.security.JwtService;
import com.collapp.project.service.AuthService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
                .systemRole(SystemRole.ROLE_CREATIVE) // todo registro público crea un usuario creativo, nunca admin
                .avatarUrl(request.avatarUrl())
                .portfolioUrl(request.portfolioUrl())
                .instagramUrl(request.instagramUrl())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(buildUserDetails(user));

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
                        request.username(),
                        request.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado tras autenticación"));

        return new AuthResponse(
                token,
                userDetails.getUsername(),
                user.getSystemRole().name()
        );
    }

    // Helper para generar el token justo tras el registro, sin pasar por AuthenticationManager
    private UserDetails buildUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getSystemRole().name())
                .build();
    }
}