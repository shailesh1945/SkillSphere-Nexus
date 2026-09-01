package com.skillsphere.nexus.learning_service.controller;

import com.skillsphere.nexus.learning_service.model.LearningCertificate;
import com.skillsphere.nexus.learning_service.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/learning/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    @PostMapping("/{enrollmentId}")
    public LearningCertificate generateCertificate(
            @PathVariable UUID enrollmentId) {
        return certificateService
                .generateCertificate(enrollmentId);
    }
}
