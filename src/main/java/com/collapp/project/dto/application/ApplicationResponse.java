package com.collapp.project.dto.application;

import com.collapp.project.entity.enums.ApplicationStatus;
import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long offerId,
        String offerTitle,
        //Para la vista "Mis Postulaciones"
        String creatorFullName,
        String creatorEmail,
        String creatorInstagramUrl,
        //Para la vista "Mis Publicaciones"
        Long applicantId,
        String applicantFullName,
        String applicantSpecialty,
        String applicantAvatarUrl,
        //La postulación
        String message,
        ApplicationStatus status,
        LocalDateTime createdAt
) {}