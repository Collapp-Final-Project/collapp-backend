package com.collapp.project.security;

import com.collapp.project.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // system_role (ej. ROLE_ADMIN) se traduce a un GrantedAuthority
        return List.of(new SimpleGrantedAuthority(user.getSystemRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // no gestionamos expiración de cuenta en este proyecto
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // no gestionamos bloqueo de cuenta
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true; //todo usuario registrado está habilitado
    }
}
