package com.collapp.project.dto.application;

import jakarta.validation.constraints.NotNull;

public record ApplicationRequest(
        @NotNull Long offerId,
        String message
) {}