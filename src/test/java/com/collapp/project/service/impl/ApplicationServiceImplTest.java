package com.collapp.project.service.impl;

import com.collapp.project.dto.application.ApplicationRequest;
import com.collapp.project.dto.application.ApplicationResponse;
import com.collapp.project.dto.application.ApplicationStatusRequest;
import com.collapp.project.entity.Application;
import com.collapp.project.entity.Offer;
import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.ApplicationStatus;
import com.collapp.project.exception.DuplicateApplicationException;
import com.collapp.project.exception.ForbiddenOperationException;
import com.collapp.project.exception.ResourceNotFoundException;
import com.collapp.project.exception.SelfApplicationException;
import com.collapp.project.mapper.ApplicationMapper;
import com.collapp.project.repository.ApplicationRepository;
import com.collapp.project.repository.OfferRepository;
import com.collapp.project.repository.UserRepository;
import com.collapp.project.util.TestObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationMapper applicationMapper;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private User creator;
    private User applicant;
    private Offer offer;
    private Application application;
    private ApplicationRequest applicationRequest;
    private ApplicationResponse applicationResponse;

    @BeforeEach
    void setUp() {
        creator = TestObjectFactory.createUser();
        applicant = TestObjectFactory.createUser2();
        offer = TestObjectFactory.createOffer(creator);
        application = TestObjectFactory.createApplication(offer, applicant);
        applicationRequest = TestObjectFactory.createApplicationRequest();
        applicationResponse = new ApplicationResponse(
                1L, 1L, "Test Offer",
                creator.getFullName(), null, null,
                applicant.getId(), applicant.getFullName(), applicant.getSpecialty().name(), applicant.getAvatarUrl(),
                "I am interested!", ApplicationStatus.PENDING, null
        );
    }

    @Test
    void create_success() {
        when(offerRepository.findById(applicationRequest.offerId())).thenReturn(Optional.of(offer));
        when(userRepository.findByEmail(applicant.getEmail())).thenReturn(Optional.of(applicant));
        when(applicationRepository.existsByOfferIdAndApplicantId(offer.getId(), applicant.getId())).thenReturn(false);
        when(applicationRepository.save(any(Application.class))).thenReturn(application);
        when(applicationMapper.toResponse(application)).thenReturn(applicationResponse);

        ApplicationResponse result = applicationService.create(applicationRequest, applicant.getEmail());

        assertNotNull(result);
        assertEquals(applicationResponse.id(), result.id());
        assertEquals(applicationResponse.status(), result.status());
        verify(applicationRepository).save(any(Application.class));
        verify(applicationMapper).toResponse(application);
    }

    @Test
    void create_selfApplication_throws() {
        when(offerRepository.findById(applicationRequest.offerId())).thenReturn(Optional.of(offer));
        when(userRepository.findByEmail(creator.getEmail())).thenReturn(Optional.of(creator));

        assertThrows(SelfApplicationException.class,
                () -> applicationService.create(applicationRequest, creator.getEmail()));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void create_duplicate_throws() {
        when(offerRepository.findById(applicationRequest.offerId())).thenReturn(Optional.of(offer));
        when(userRepository.findByEmail(applicant.getEmail())).thenReturn(Optional.of(applicant));
        when(applicationRepository.existsByOfferIdAndApplicantId(offer.getId(), applicant.getId())).thenReturn(true);

        assertThrows(DuplicateApplicationException.class,
                () -> applicationService.create(applicationRequest, applicant.getEmail()));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void create_offerNotFound_throws() {
        when(offerRepository.findById(applicationRequest.offerId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.create(applicationRequest, applicant.getEmail()));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void create_userNotFound_throws() {
        when(offerRepository.findById(applicationRequest.offerId())).thenReturn(Optional.of(offer));
        when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.create(applicationRequest, "nonexistent@test.com"));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void listByOffer_success() {
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));
        when(applicationRepository.findByOfferId(offer.getId())).thenReturn(List.of(application));
        when(applicationMapper.toResponseList(List.of(application))).thenReturn(List.of(applicationResponse));

        List<ApplicationResponse> result = applicationService.listByOffer(offer.getId(), creator.getEmail());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(applicationRepository).findByOfferId(offer.getId());
    }

    @Test
    void listByOffer_notOwner_throws() {
        when(offerRepository.findById(offer.getId())).thenReturn(Optional.of(offer));

        assertThrows(ForbiddenOperationException.class,
                () -> applicationService.listByOffer(offer.getId(), applicant.getEmail()));

        verify(applicationRepository, never()).findByOfferId(any());
    }

    @Test
    void listMine_success() {
        when(userRepository.findByEmail(applicant.getEmail())).thenReturn(Optional.of(applicant));
        when(applicationRepository.findByApplicantId(applicant.getId())).thenReturn(List.of(application));
        when(applicationMapper.toResponseList(List.of(application))).thenReturn(List.of(applicationResponse));

        List<ApplicationResponse> result = applicationService.listMine(applicant.getEmail());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(applicationRepository).findByApplicantId(applicant.getId());
    }

    @Test
    void updateStatus_success() {
        ApplicationStatusRequest statusRequest = TestObjectFactory.createApplicationStatusRequest(ApplicationStatus.ACCEPTED);
        ApplicationResponse acceptedResponse = new ApplicationResponse(
                1L, 1L, "Test Offer",
                creator.getFullName(), creator.getEmail(), creator.getInstagramUrl(),
                applicant.getId(), applicant.getFullName(), applicant.getSpecialty().name(), applicant.getAvatarUrl(),
                "I am interested!", ApplicationStatus.ACCEPTED, null
        );

        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);
        when(applicationMapper.toResponse(application)).thenReturn(acceptedResponse);

        ApplicationResponse result = applicationService.updateStatus(application.getId(), statusRequest, creator.getEmail());

        assertNotNull(result);
        assertEquals(ApplicationStatus.ACCEPTED, result.status());
        verify(applicationRepository).save(application);
    }

    @Test
    void updateStatus_notOwner_throws() {
        ApplicationStatusRequest statusRequest = TestObjectFactory.createApplicationStatusRequest(ApplicationStatus.ACCEPTED);

        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));

        assertThrows(ForbiddenOperationException.class,
                () -> applicationService.updateStatus(application.getId(), statusRequest, applicant.getEmail()));

        verify(applicationRepository, never()).save(any());
    }

    @Test
    void updateStatus_applicationNotFound_throws() {
        ApplicationStatusRequest statusRequest = TestObjectFactory.createApplicationStatusRequest(ApplicationStatus.ACCEPTED);

        when(applicationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> applicationService.updateStatus(999L, statusRequest, creator.getEmail()));
    }
}
