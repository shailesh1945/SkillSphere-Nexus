package com.skillsphere.nexus.skill_service.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationAuditResponse {

    private UUID auditId;

    private UUID certificationId;

    private UUID employeeId;

    private String action;

    private String performedBy;

    private LocalDateTime performedAt;
}
