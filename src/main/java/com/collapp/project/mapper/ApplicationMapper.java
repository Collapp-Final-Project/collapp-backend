package com.collapp.project.mapper;

import com.collapp.project.dto.application.ApplicationResponse;
import com.collapp.project.entity.Application;
import com.collapp.project.entity.enums.ApplicationStatus;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationResponse toResponse(Application application) {
        boolean isAccepted = application.getStatus() == ApplicationStatus.ACCEPTED;

        return new ApplicationResponse(
                application.getId(),
                application.getOffer().getId(),
                application.getOffer().getTitle(),
                // Solo se revela el contacto si la postulación fue aceptada
                application.getOffer().getCreator().getFullName(),
                isAccepted ? application.getOffer().getCreator().getEmail() : null,
                isAccepted ? application.getOffer().getCreator().getInstagramUrl() : null,
                // El postulante
                application.getApplicant().getId(),
                application.getApplicant().getFullName(),
                application.getApplicant().getSpecialty() != null ? application.getApplicant().getSpecialty().name() : null,
                application.getApplicant().getAvatarUrl(),
                // La postulación
                application.getMessage(),
                application.getStatus(),
                application.getCreatedAt()
        );
    }
}