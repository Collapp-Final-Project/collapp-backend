package com.collapp.project.dto.auth;

import com.collapp.project.entity.enums.Specialty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        // Pantalla 1 — datos obligatorios
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 3, max = 30, message = "El username debe tener entre 3 y 30 caracteres")
        String username,

        @NotBlank(message = "El nombre completo es obligatorio")
        String fullName,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotNull(message = "La especialidad es obligatoria")
        Specialty specialty,

        // Pantalla 2 — datos opcionales
        String avatarUrl,
        String portfolioUrl,
        String instagramUrl
) {}