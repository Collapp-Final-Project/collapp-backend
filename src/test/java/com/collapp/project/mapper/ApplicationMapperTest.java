package com.collapp.project.mapper;

import com.collapp.project.dto.application.ApplicationResponse;
import com.collapp.project.entity.Application;
import com.collapp.project.entity.Offer;
import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.ApplicationStatus;
import com.collapp.project.util.TestObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApplicationMapperTest {

    private ApplicationMapper applicationMapper;
    private User creator;
    private User applicant;
    private Offer offer;

    @BeforeEach
    void setUp() {
        applicationMapper = new ApplicationMapper();
        creator = TestObjectFactory.createUser();
        applicant = TestObjectFactory.createUser2();
        offer = TestObjectFactory.createOffer(creator);
    }

    @Test
    void toResponse_pending_hidesContact() {
        Application application = TestObjectFactory.createApplication(1L, offer, applicant, ApplicationStatus.PENDING);

        ApplicationResponse response = applicationMapper.toResponse(application);

        assertNotNull(response);
        assertEquals(ApplicationStatus.PENDING, response.status());
        assertEquals(creator.getFullName(), response.creatorFullName());
        assertNull(response.creatorEmail());
        assertNull(response.creatorInstagramUrl());
    }

    @Test
    void toResponse_rejected_hidesContact() {
        Application application = TestObjectFactory.createApplication(1L, offer, applicant, ApplicationStatus.REJECTED);

        ApplicationResponse response = applicationMapper.toResponse(application);

        assertEquals(ApplicationStatus.REJECTED, response.status());
        assertNull(response.creatorEmail());
        assertNull(response.creatorInstagramUrl());
    }

    @Test
    void toResponse_accepted_showsContact() {
        Application application = TestObjectFactory.createApplication(1L, offer, applicant, ApplicationStatus.ACCEPTED);

        ApplicationResponse response = applicationMapper.toResponse(application);

        assertEquals(ApplicationStatus.ACCEPTED, response.status());
        assertEquals(creator.getEmail(), response.creatorEmail());
        assertEquals(creator.getInstagramUrl(), response.creatorInstagramUrl());
    }

    @Test
    void toResponse_mapsAllFields() {
        Application application = TestObjectFactory.createApplication(offer, applicant);

        ApplicationResponse response = applicationMapper.toResponse(application);

        assertNotNull(response);
        assertEquals(application.getId(), response.id());
        assertEquals(offer.getId(), response.offerId());
        assertEquals(offer.getTitle(), response.offerTitle());
        assertEquals(applicant.getId(), response.applicantId());
        assertEquals(applicant.getFullName(), response.applicantFullName());
        assertEquals(applicant.getSpecialty().name(), response.applicantSpecialty());
        assertEquals(applicant.getAvatarUrl(), response.applicantAvatarUrl());
        assertEquals(application.getMessage(), response.message());
        assertEquals(application.getStatus(), response.status());
    }
}
