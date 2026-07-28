package com.collapp.project.dto.offer;

import com.collapp.project.entity.enums.CompensationType;
import com.collapp.project.entity.enums.OfferStatus;
import com.collapp.project.entity.enums.Specialty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record OfferResponse(
        Long id,
        Long creatorId,
        String creatorUsername,
        String creatorAvatarUrl,
        String title,
        String description,
        Specialty category,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        CompensationType compensationType,
        OfferStatus status,
        LocalDateTime createdAt
) {}