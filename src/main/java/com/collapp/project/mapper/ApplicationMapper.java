package com.collapp.project.mapper;

import com.collapp.project.dto.application.ApplicationResponse;
import com.collapp.project.entity.Application;
import com.collapp.project.entity.enums.ApplicationStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationMapper {

    public ApplicationResponse toResponse(Application application) {
        boolean isAccepted = application.getStatus() == ApplicationStatus.ACCEPTED;

        String creatorEmail = isAccepted
                ? application.getOffer().getCreator().getEmail()
                : null;

        String creatorInstagramUrl = isAccepted
                ? application.getOffer().getCreator().getInstagramUrl()
                : null;

        String applicantSpecialtyName = application.getApplicant().getSpecialty() != null
                ? application.getApplicant().getSpecialty().name()
                : null;

        return new ApplicationResponse(
                application.getId(),
                application.getOffer().getId(),
                application.getOffer().getTitle(),
                application.getOffer().getCreator().getFullName(),
                creatorEmail,
                creatorInstagramUrl,
                application.getApplicant().getId(),
                application.getApplicant().getFullName(),
                applicantSpecialtyName,
                application.getApplicant().getAvatarUrl(),
                application.getMessage(),
                application.getStatus(),
                application.getCreatedAt()
        );
    }

    public List<ApplicationResponse> toResponseList(List<Application> applications) {
        return applications.stream()
                .map(this::toResponse)
                .toList();
    }
}