package com.collapp.project.dto.offer;

import com.collapp.project.entity.Offer;
import com.collapp.project.entity.enums.CompensationType;
import com.collapp.project.entity.enums.OfferStatus;
import com.collapp.project.entity.enums.Specialty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OfferResponse(
        Long id,
        Long creatorId,
        String creatorUsername,
        String title,
        String description,
        Specialty category,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        CompensationType compensationType,
        OfferStatus status,
        LocalDateTime createdAt
) {
    // Mapper estático simple — evita crear una clase mapper/ separada para una sola entidad,
    // coherente con la decisión ya tomada de no usar mapper/ hasta que haga falta de verdad
    public static OfferResponse fromEntity(Offer offer) {
        return new OfferResponse(
                offer.getId(),
                offer.getCreator().getId(),
                offer.getCreator().getUsername(),
                offer.getTitle(),
                offer.getDescription(),
                offer.getCategory(),
                offer.getLocation(),
                offer.getStartDate(),
                offer.getEndDate(),
                offer.getCompensationType(),
                offer.getStatus(),
                offer.getCreatedAt()
        );
    }
}