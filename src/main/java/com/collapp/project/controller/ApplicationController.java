package com.collapp.project.controller;
import com.collapp.project.dto.application.ApplicationRequest;
import com.collapp.project.dto.application.ApplicationResponse;
import com.collapp.project.dto.application.ApplicationStatusRequest;
import com.collapp.project.security.CustomUserDetails;
import com.collapp.project.service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @Valid @RequestBody ApplicationRequest request,
            Authentication authentication) {
        ApplicationResponse response = applicationService.create(request, extractEmail(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ApplicationResponse>> listMine(Authentication authentication) {
        return ResponseEntity.ok(applicationService.listMine(extractEmail(authentication)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                applicationService.updateStatus(id, request, extractEmail(authentication))
        );
    }

    private String extractEmail(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUser().getEmail();
    }

}
