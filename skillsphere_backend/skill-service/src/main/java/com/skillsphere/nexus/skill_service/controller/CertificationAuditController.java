package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.response.CertificationAuditResponse;
import com.skillsphere.nexus.skill_service.service.CertificationAuditService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationAuditController {

    private final CertificationAuditService auditService;


    @GetMapping("/{certificationId}/audit")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public List<CertificationAuditResponse> getAudit(
            @PathVariable UUID certificationId) {

        return auditService
                .getAudit(certificationId);
    }
}