package com.collapp.project.controller;

import com.collapp.project.dto.application.ApplicationResponse;
import com.collapp.project.dto.offer.OfferRequest;
import com.collapp.project.dto.offer.OfferResponse;
import com.collapp.project.dto.offer.OfferStatusRequest;
import com.collapp.project.entity.enums.Specialty;
import com.collapp.project.security.CustomUserDetails;
import com.collapp.project.service.ApplicationService;
import com.collapp.project.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<Page<OfferResponse>> list(
            @RequestParam(required = false) Specialty category,
            Pageable pageable) {
        return ResponseEntity.ok(offerService.list(category, pageable));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<OfferResponse>> listMyOffers(Authentication authentication) {
        return ResponseEntity.ok(offerService.listMine(extractEmail(authentication)));
    }

    @GetMapping("/all")
    public ResponseEntity<List<OfferResponse>> listAll() {
        return ResponseEntity.ok(offerService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(offerService.getById(id));
    }

    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationResponse>> listApplicationsForOffer(
            @PathVariable Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                applicationService.listByOffer(id, extractEmail(authentication)));
    }

    @PostMapping
    public ResponseEntity<OfferResponse> create(
            @Valid @RequestBody OfferRequest request,
            Authentication authentication) {
        OfferResponse response = offerService.create(request, extractEmail(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody OfferRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(offerService.update(id, request, extractEmail(authentication)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        offerService.delete(id, extractEmail(authentication), isAdmin(authentication));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OfferResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody OfferStatusRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(offerService.updateStatus(id, request.status(), extractEmail(authentication)));
    }

    // Extrae el email del usuario autenticado
    private String extractEmail(Authentication authentication) {
        return ((CustomUserDetails) authentication.getPrincipal()).getUser().getEmail();
    }

    // Comprueba si el usuario autenticado tiene rol de administrador
    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}