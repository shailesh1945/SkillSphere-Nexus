package com.skillsphere.nexus.skill_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "certification_audits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificationAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID auditId;

    private UUID certificationId;

    private UUID employeeId;

    private String action;

    private String performedBy;

    private LocalDateTime performedAt;
}