package com.collapp.project.controller;

import com.collapp.project.dto.user.UserResponse;
import com.collapp.project.security.CustomUserDetails;
import com.collapp.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
        String email = ((CustomUserDetails) authentication.getPrincipal()).getUser().getEmail();
        return ResponseEntity.ok(userService.getCurrentUser(email));
    }
}