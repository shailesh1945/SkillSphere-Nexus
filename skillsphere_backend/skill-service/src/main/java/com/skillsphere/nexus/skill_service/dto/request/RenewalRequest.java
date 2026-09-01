package com.skillsphere.nexus.skill_service.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalRequest {

    private String requestedBy;

    private LocalDate newExpiry;

    private String approvedBy;
}