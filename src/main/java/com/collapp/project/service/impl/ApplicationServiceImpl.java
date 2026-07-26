package com.collapp.project.service.impl;
import com.collapp.project.dto.application.ApplicationRequest;
import com.collapp.project.dto.application.ApplicationResponse;
import com.collapp.project.dto.application.ApplicationStatusRequest;
import com.collapp.project.entity.Application;
import com.collapp.project.entity.enums.ApplicationStatus;
import com.collapp.project.entity.Offer;
import com.collapp.project.entity.User;
import com.collapp.project.exception.DuplicateApplicationException;
import com.collapp.project.exception.ForbiddenOperationException;
import com.collapp.project.exception.ResourceNotFoundException;
import com.collapp.project.exception.SelfApplicationException;
import com.collapp.project.mapper.ApplicationMapper;
import com.collapp.project.repository.ApplicationRepository;
import com.collapp.project.repository.OfferRepository;
import com.collapp.project.repository.UserRepository;
import com.collapp.project.service.ApplicationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ApplicationMapper applicationMapper;

    @Override
    public ApplicationResponse create(ApplicationRequest request, String applicantEmail) {
        Offer offer = findOfferOrThrow(request.offerId());
        User applicant = findUserByEmailOrThrow(applicantEmail);

        validateNotOwnOffer(offer, applicant);
        validateNotAlreadyApplied(offer, applicant);

        Application application = new Application();
        application.setOffer(offer);
        application.setApplicant(applicant);
        application.setMessage(request.message());
        application.setStatus(ApplicationStatus.PENDING);

        Application saved = applicationRepository.save(application);
        return applicationMapper.toResponse(saved);
    }

    @Override
    public List<ApplicationResponse> listByOffer(Long offerId, String requesterEmail) {
        Offer offer = findOfferOrThrow(offerId);
        validateOwnership(offer, requesterEmail);

        return applicationRepository.findByOfferId(offerId).stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    @Override
    public List<ApplicationResponse> listMine(String applicantEmail) {
        User applicant = findUserByEmailOrThrow(applicantEmail);

        return applicationRepository.findByApplicantId(applicant.getId()).stream()
                .map(applicationMapper::toResponse)
                .toList();
    }

    @Override
    public ApplicationResponse updateStatus(Long applicationId, ApplicationStatusRequest request, String requesterEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        validateOwnership(application.getOffer(), requesterEmail);

        application.setStatus(request.status());
        Application updated = applicationRepository.save(application);
        return applicationMapper.toResponse(updated);
    }


    // -- Validación y búsqueda ---

    private Offer findOfferOrThrow(Long offerId) {
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
    }

    private User findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateNotOwnOffer(Offer offer, User applicant) {
        if (offer.getCreator().getEmail().equals(applicant.getEmail())) {
            throw new SelfApplicationException("You cannot apply to your own offer");
        }
    }

    private void validateNotAlreadyApplied(Offer offer, User applicant) {
        boolean alreadyApplied = applicationRepository
                .existsByOfferIdAndApplicantId(offer.getId(), applicant.getId());

        if (alreadyApplied) {
            throw new DuplicateApplicationException("You have already applied to this offer");
        }
    }

    private void validateOwnership(Offer offer, String requesterEmail) {
        if (!offer.getCreator().getEmail().equals(requesterEmail)) {
            throw new ForbiddenOperationException("You do not own this offer");
        }
    }
}
