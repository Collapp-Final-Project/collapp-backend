package com.collapp.project.dto.offer;

import com.collapp.project.entity.enums.OfferStatus;
import jakarta.validation.constraints.NotNull;

public record OfferStatusRequest(
        @NotNull(message = "El estado es obligatorio")
        OfferStatus status
) {}