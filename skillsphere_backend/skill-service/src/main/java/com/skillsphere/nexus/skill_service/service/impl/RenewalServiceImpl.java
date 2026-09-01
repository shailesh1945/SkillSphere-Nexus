package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.config.*;
import com.skillsphere.nexus.skill_service.dto.request.RenewalRequest;
import com.skillsphere.nexus.skill_service.dto.response.RenewalResponse;
import com.skillsphere.nexus.skill_service.model.Certification;
import com.skillsphere.nexus.skill_service.model.CertificationRenewal;
import com.skillsphere.nexus.skill_service.repository.CertificationRenewalRepository;
import com.skillsphere.nexus.skill_service.repository.CertificationRepository;
import com.skillsphere.nexus.skill_service.service.CertificationAuditService;
import com.skillsphere.nexus.skill_service.service.RenewalService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RenewalServiceImpl implements RenewalService{

    private final CertificationRepository certificationRepository;

    private final CertificationRenewalRepository
            renewalRepository;

    private final CertificationAuditService auditService;

    private final KafkaCertificationProducer kafkaProducer;


    @Override
    public RenewalResponse requestRenewal(
            UUID certificationId,
            String requestedBy) {

        Certification certification =
                certificationRepository
                        .findById(certificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Certification not found"));

        CertificationRenewal renewal =
                CertificationRenewal.builder()
                        .certification(certification)
                        .oldExpiry(
                                certification.getExpiryDate())
                        .status(
                                CertificationRenewal
                                        .RenewalStatus.REQUESTED)
                        .requestedBy(requestedBy)
                        .requestedAt(
                                LocalDateTime.now())
                        .build();

        CertificationRenewal saved =
                renewalRepository.save(renewal);

        // Publish Kafka event
        kafkaProducer.sendRenewalEvent(
                "Certification renewal requested: "
                        + certification.getCertificationName()
                        + " for employee "
                        + certification.getEmployee().getFirstName()
        );

        auditService.log(
                certification.getCertificationId(),
                certification.getEmployee()
                        .getEmployeeId(),
                "RENEWAL_REQUESTED",
                requestedBy);

        return mapToResponse(saved);
    }

    @Override
    public RenewalResponse approveRenewal(
            UUID renewalId,
            RenewalRequest request) {

        CertificationRenewal renewal =
                renewalRepository.findById(renewalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Renewal not found"));

        Certification certification =
                renewal.getCertification();

        certification.setExpiryDate(
                request.getNewExpiry());

        certification.setStatus(
                Certification.Status.VALID);

        certificationRepository.save(certification);


        renewal.setNewExpiry(
                request.getNewExpiry());

        renewal.setApprovedBy(
                request.getApprovedBy());

        renewal.setApprovedAt(
                LocalDateTime.now());

        renewal.setStatus(
                CertificationRenewal
                        .RenewalStatus.APPROVED);


        CertificationRenewal saved =
                renewalRepository.save(renewal);


        auditService.log(
                certification.getCertificationId(),
                certification.getEmployee()
                        .getEmployeeId(),
                "RENEWED",
                request.getApprovedBy());

        return mapToResponse(saved);
    }


    private RenewalResponse mapToResponse(
            CertificationRenewal renewal) {

        return RenewalResponse.builder()
                .renewalId(
                        renewal.getRenewalId())
                .certificationId(
                        renewal.getCertification()
                                .getCertificationId())
                .oldExpiry(
                        renewal.getOldExpiry())
                .newExpiry(
                        renewal.getNewExpiry())
                .status(
                        renewal.getStatus().name())
                .requestedBy(
                        renewal.getRequestedBy())
                .approvedBy(
                        renewal.getApprovedBy())
                .requestedAt(
                        renewal.getRequestedAt())
                .approvedAt(
                        renewal.getApprovedAt())
                .build();
    }
}