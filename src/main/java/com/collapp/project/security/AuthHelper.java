package com.collapp.project.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    public String extractEmail(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUser().getEmail();
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
