package com.collapp.project.service;

import com.collapp.project.dto.application.ApplicationRequest;
import com.collapp.project.dto.application.ApplicationResponse;
import com.collapp.project.dto.application.ApplicationStatusRequest;

import java.util.List;

public interface ApplicationService {

    ApplicationResponse create(ApplicationRequest request, String applicantEmail);
    List<ApplicationResponse> listByOffer(Long offerId, String requesterEmail);
    List<ApplicationResponse> listMine(String applicantEmail);
    ApplicationResponse updateStatus(Long applicationId, ApplicationStatusRequest request, String requesterEmail);
}

