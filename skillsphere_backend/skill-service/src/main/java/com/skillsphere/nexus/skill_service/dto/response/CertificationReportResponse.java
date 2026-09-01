package com.skillsphere.nexus.skill_service.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationReportResponse {

    private long total;

    private long valid;

    private long expired;

    private long pendingRenewal;

    private long expiringWithin30Days;

    private double renewalRate;
}
