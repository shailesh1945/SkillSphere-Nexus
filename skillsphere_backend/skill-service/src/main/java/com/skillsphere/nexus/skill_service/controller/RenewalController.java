package com.skillsphere.nexus.skill_service.controller;

import com.skillsphere.nexus.skill_service.dto.request.RenewalRequest;
import com.skillsphere.nexus.skill_service.dto.response.RenewalResponse;
import com.skillsphere.nexus.skill_service.service.RenewalService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certifications/renewals")
@RequiredArgsConstructor
public class RenewalController {

    private final RenewalService renewalService;


    @PostMapping("/{certificationId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'ADMIN')")
    public RenewalResponse requestRenewal(
            @PathVariable UUID certificationId,
            @RequestParam String requestedBy) {

        return renewalService.requestRenewal(
                certificationId,
                requestedBy);
    }


    @PutMapping("/{renewalId}/approve")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public RenewalResponse approveRenewal(
            @PathVariable UUID renewalId,
            @RequestParam("newExpiry") LocalDate newExpiry,
            @RequestParam("approvedBy") String approvedBy) {

        RenewalRequest request = RenewalRequest.builder()
                .newExpiry(newExpiry)
                .approvedBy(approvedBy)
                .build();

        return renewalService.approveRenewal(
                renewalId,
                request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    public List<RenewalResponse> getRenewalRequests() {

        return renewalService.getRenewalRequests();
    }
}