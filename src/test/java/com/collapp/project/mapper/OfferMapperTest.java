package com.collapp.project.mapper;

import com.collapp.project.dto.offer.OfferResponse;
import com.collapp.project.entity.Offer;
import com.collapp.project.entity.User;
import com.collapp.project.util.TestObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OfferMapperTest {

    private OfferMapper offerMapper;
    private Offer offer;
    private User creator;

    @BeforeEach
    void setUp() {
        offerMapper = new OfferMapper();
        creator = TestObjectFactory.createUser();
        offer = TestObjectFactory.createOffer(creator);
    }

    @Test
    void toResponse_mapsAllFields() {
        OfferResponse response = offerMapper.toResponse(offer);

        assertNotNull(response);
        assertEquals(offer.getId(), response.id());
        assertEquals(offer.getCreator().getId(), response.creatorId());
        assertEquals(offer.getCreator().getUsername(), response.creatorUsername());
        assertEquals(offer.getCreator().getAvatarUrl(), response.creatorAvatarUrl());
        assertEquals(offer.getTitle(), response.title());
        assertEquals(offer.getDescription(), response.description());
        assertEquals(offer.getCategory(), response.category());
        assertEquals(offer.getLocation(), response.location());
        assertEquals(offer.getStartDate(), response.startDate());
        assertEquals(offer.getEndDate(), response.endDate());
        assertEquals(offer.getCompensationType(), response.compensationType());
        assertEquals(offer.getStatus(), response.status());
    }
}
