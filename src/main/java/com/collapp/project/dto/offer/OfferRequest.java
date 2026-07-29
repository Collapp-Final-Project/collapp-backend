package com.collapp.project.dto.offer;

import com.collapp.project.entity.enums.OfferStatus;
import com.collapp.project.entity.enums.CompensationType;
import com.collapp.project.entity.enums.Specialty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

public record OfferRequest(

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotNull(message = "La categoría es obligatoria")
        Specialty category,

        @NotBlank(message = "La ubicación es obligatoria")
        String location,

        @NotNull(message = "La fecha de inicio es obligatoria")
        @FutureOrPresent(message = "La fecha de inicio no puede ser pasada")
        LocalDate startDate,

        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate endDate,

        @NotNull(message = "El tipo de compensación es obligatorio")
        CompensationType compensationType,

        @NotNull(message = "El estado es obligatorio")
        OfferStatus status

) {}