package com.skillsphere.nexus.skill_service.dto.response;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenewalResponse {

    private UUID renewalId;

    private UUID certificationId;

    private LocalDate oldExpiry;

    private LocalDate newExpiry;

    private String status;

    private String requestedBy;

    private String approvedBy;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;
}