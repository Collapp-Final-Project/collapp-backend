package com.collapp.project.repository;
import com.collapp.project.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByOfferIdAndApplicantId(Long offerId, Long applicantId);

    List<Application> findByOfferId(Long offerId);

    List<Application> findByApplicantId(Long applicantId);
}
