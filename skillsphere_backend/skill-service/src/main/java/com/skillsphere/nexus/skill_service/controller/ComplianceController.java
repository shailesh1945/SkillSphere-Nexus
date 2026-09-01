package com.skillsphere.nexus.skill_service.controller;


import com.skillsphere.nexus.skill_service.dto.response.ComplianceResponse;
import com.skillsphere.nexus.skill_service.service.ComplianceService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/certifications/compliance")
@RequiredArgsConstructor
public class ComplianceController {

    private final ComplianceService complianceService;


    @GetMapping("/{employeeId}")
    public ComplianceResponse getCompliance(
            @PathVariable UUID employeeId) {

        return complianceService
                .getCompliance(employeeId);
    }
}