package com.collapp.project.service.impl;

import com.collapp.project.dto.offer.OfferRequest;
import com.collapp.project.dto.offer.OfferResponse;
import com.collapp.project.entity.Offer;
import com.collapp.project.entity.User;
import com.collapp.project.entity.enums.OfferStatus;
import com.collapp.project.entity.enums.Specialty;
import com.collapp.project.exception.ForbiddenOperationException;
import com.collapp.project.exception.ResourceNotFoundException;
import com.collapp.project.repository.OfferRepository;
import com.collapp.project.repository.UserRepository;
import com.collapp.project.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    @Override
    public OfferResponse create(OfferRequest request, String creatorEmail) {
        User creator = findUserByEmail(creatorEmail);

        Offer offer = Offer.builder()
                .creator(creator)
                .title(request.title())
                .description(request.description())
                .category(request.category())
                .location(request.location())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .compensationType(request.compensationType())
                .status(OfferStatus.OPEN)
                .build();

        return OfferResponse.fromEntity(offerRepository.save(offer));
    }

    @Override
    public OfferResponse update(Long offerId, OfferRequest request, String requesterEmail) {
        Offer offer = findOfferById(offerId);
        assertIsOwner(offer, requesterEmail);

        offer.setTitle(request.title());
        offer.setDescription(request.description());
        offer.setCategory(request.category());
        offer.setLocation(request.location());
        offer.setStartDate(request.startDate());
        offer.setEndDate(request.endDate());
        offer.setCompensationType(request.compensationType());

        return OfferResponse.fromEntity(offerRepository.save(offer));
    }

    @Override
    public OfferResponse updateStatus(Long offerId, OfferStatus status, String requesterEmail) {
        Offer offer = findOfferById(offerId);
        assertIsOwner(offer, requesterEmail);
        offer.setStatus(status);
        return OfferResponse.fromEntity(offerRepository.save(offer));
    }

    @Override
    public void delete(Long offerId, String requesterEmail) {
        Offer offer = findOfferById(offerId);
        assertIsOwner(offer, requesterEmail);
        offerRepository.delete(offer);
    }

    @Override
    public OfferResponse getById(Long offerId) {
        return OfferResponse.fromEntity(findOfferById(offerId));
    }

    @Override
    public Page<OfferResponse> list(Specialty category, Pageable pageable) {
        Page<Offer> offers = (category != null)
                ? offerRepository.findByStatusAndCategory(OfferStatus.OPEN, category, pageable)
                : offerRepository.findByStatus(OfferStatus.OPEN, pageable);

        return offers.map(OfferResponse::fromEntity);
    }

    // --- Helpers ---

    private Offer findOfferById(Long id) {
        return offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada con id: " + id));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    private void assertIsOwner(Offer offer, String requesterEmail) {
        if (!offer.getCreator().getEmail().equals(requesterEmail)) {
            throw new ForbiddenOperationException("No tienes permiso para modificar esta oferta");
        }
    }
}