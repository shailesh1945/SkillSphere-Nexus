package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.request.RenewalRequest;
import com.skillsphere.nexus.skill_service.dto.response.RenewalResponse;
import com.skillsphere.nexus.skill_service.service.RenewalService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/certifications/renewals")
@RequiredArgsConstructor
public class RenewalController {

    private final RenewalService renewalService;


    @PostMapping("/{certificationId}")
    public RenewalResponse requestRenewal(
            @PathVariable UUID certificationId,
            @RequestParam String requestedBy) {

        return renewalService.requestRenewal(
                certificationId,
                requestedBy);
    }


    @PutMapping("/{renewalId}/approve")
    public RenewalResponse approveRenewal(
            @PathVariable UUID renewalId,
            @RequestBody RenewalRequest request) {

        return renewalService.approveRenewal(
                renewalId,
                request);
    }
}