package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.request.CertificationRequest;
import com.skillsphere.nexus.skill_service.dto.response.CertificationResponse;
import com.skillsphere.nexus.skill_service.service.CertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;

    // Add certification
    @PostMapping
    public ResponseEntity<CertificationResponse> addCertification(
            @Valid @RequestBody CertificationRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(certificationService.addCertification(request));
    }

    // Update certification
    @PutMapping("/{certificationId}")
    public ResponseEntity<CertificationResponse> updateCertification(
            @PathVariable UUID certificationId,
            @Valid @RequestBody CertificationRequest request) {

        return ResponseEntity.ok(
                certificationService.updateCertification(
                        certificationId,
                        request
                )
        );
    }

    // Get certification by ID
    @GetMapping("/{certificationId}")
    public ResponseEntity<CertificationResponse> getCertificationById(
            @PathVariable UUID certificationId) {

        return ResponseEntity.ok(
                certificationService.getCertificationById(certificationId)
        );
    }

    // Get all certifications
    @GetMapping
    public ResponseEntity<List<CertificationResponse>> getAllCertifications() {

        return ResponseEntity.ok(
                certificationService.getAllCertifications()
        );
    }

    // Delete certification
    @DeleteMapping("/{certificationId}")
    public ResponseEntity<Void> deleteCertification(
            @PathVariable UUID certificationId) {

        certificationService.deleteCertification(certificationId);

        return ResponseEntity.noContent().build();
    }

    // Get certifications of an employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<CertificationResponse>> getEmployeeCertifications(
            @PathVariable UUID employeeId) {

        return ResponseEntity.ok(
                certificationService.getEmployeeCertifications(employeeId)
        );
    }

    // Get active/valid certifications
    @GetMapping("/active")
    public ResponseEntity<List<CertificationResponse>> getActiveCertifications() {

        return ResponseEntity.ok(
                certificationService.getActiveCertifications()
        );
    }

    // Get expired certifications
    @GetMapping("/expired")
    public ResponseEntity<List<CertificationResponse>> getExpiredCertifications() {

        return ResponseEntity.ok(
                certificationService.getExpiredCertifications()
        );
    }

    // Get certifications expiring within specified days
    @GetMapping("/expiring")
    public ResponseEntity<List<CertificationResponse>> getCertificationsExpiringWithin(
            @RequestParam(defaultValue = "30") int days) {

        return ResponseEntity.ok(
                certificationService.getCertificationsExpiringWithin(days)
        );
    }

    // Renew certification
//    @PutMapping("/{certificationId}/renew")
//    public ResponseEntity<CertificationResponse> renewCertification(
//            @PathVariable UUID certificationId,
//            @RequestBody CertificationRequest request) {
//
//        return ResponseEntity.ok(
//                certificationService.renewCertification(
//                        certificationId,
//                        request
//                )
//        );
//    }
}