package com.collapp.project.mapper;

import com.collapp.project.dto.offer.OfferResponse;
import com.collapp.project.entity.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    public OfferResponse toResponse(Offer offer) {
        return new OfferResponse(
                offer.getId(),
                offer.getCreator().getId(),
                offer.getCreator().getUsername(),
                offer.getCreator().getAvatarUrl(),
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