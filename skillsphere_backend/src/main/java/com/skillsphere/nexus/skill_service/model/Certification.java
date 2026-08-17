package com.skillsphere.nexus.skill_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "certifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID certificationId;

    @Column(nullable = false)
    private String certificationName;

    private String issuingOrganization;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String credentialId;

    private String credentialUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public enum Status {
        VALID,
        EXPIRED,
        PENDING_RENEWAL,
    }
}
