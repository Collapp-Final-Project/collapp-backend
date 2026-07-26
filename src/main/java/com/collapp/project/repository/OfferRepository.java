package com.collapp.project.repository;

import com.collapp.project.entity.Offer;
import com.collapp.project.entity.enums.OfferStatus;
import com.collapp.project.entity.enums.Specialty;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {

    Page<Offer> findByStatus(OfferStatus status, Pageable pageable);

    Page<Offer> findByStatusAndCategory(OfferStatus status, Specialty category, Pageable pageable);

    List<Offer> findByCreatorId(Long creatorId);
}
