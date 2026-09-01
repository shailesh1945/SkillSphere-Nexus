package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.response.CertificationReportResponse;
import com.skillsphere.nexus.skill_service.service.CertificationReportService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certifications/report")
@RequiredArgsConstructor
public class CertificationReportController {

    private final CertificationReportService
            reportService;


    @GetMapping
    public CertificationReportResponse generateReport() {

        return reportService.generateReport();
    }
}