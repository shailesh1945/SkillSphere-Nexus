package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.response.CertificationReportResponse;
import com.skillsphere.nexus.skill_service.service.CertificationReportService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certifications/report")
@RequiredArgsConstructor
public class CertificationReportController {

    private final CertificationReportService
            reportService;


    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public CertificationReportResponse generateReport() {

        return reportService.generateReport();
    }
}