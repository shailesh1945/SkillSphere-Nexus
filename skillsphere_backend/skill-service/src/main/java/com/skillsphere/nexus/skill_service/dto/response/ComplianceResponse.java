package com.skillsphere.nexus.skill_service.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceResponse {

    private String employeeName;

    private long totalCertifications;

    private long validCertifications;

    private long expiredCertifications;

    private boolean compliant;
}