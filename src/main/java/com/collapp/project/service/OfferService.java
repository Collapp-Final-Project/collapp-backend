package com.collapp.project.service;

import com.collapp.project.dto.offer.OfferRequest;
import com.collapp.project.dto.offer.OfferResponse;
import com.collapp.project.entity.enums.OfferStatus;
import com.collapp.project.entity.enums.Specialty;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfferService {

    OfferResponse create(OfferRequest request, String creatorEmail);

    OfferResponse update(Long offerId, OfferRequest request, String requesterEmail);

    void delete(Long offerId, String requesterEmail);

    OfferResponse getById(Long offerId);

    Page<OfferResponse> list(Specialty category, Pageable pageable);

    OfferResponse updateStatus(Long offerId, OfferStatus status, String requesterEmail);

    List<OfferResponse> listMine(String creatorEmail);
}