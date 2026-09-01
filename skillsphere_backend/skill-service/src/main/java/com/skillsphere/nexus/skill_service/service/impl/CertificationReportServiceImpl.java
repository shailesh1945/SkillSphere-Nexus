package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.response.CertificationReportResponse;
import com.skillsphere.nexus.skill_service.model.Certification;
import com.skillsphere.nexus.skill_service.repository.CertificationRepository;
import com.skillsphere.nexus.skill_service.service.CertificationReportService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationReportServiceImpl implements CertificationReportService {

    private final CertificationRepository
            certificationRepository;

    @Override
    public CertificationReportResponse generateReport() {

        List<Certification> certifications =
                certificationRepository.findAll();


        long total =
                certifications.size();


        long valid =
                certifications.stream()
                        .filter(cert ->
                                cert.getStatus()
                                        == Certification.Status.VALID)
                        .count();


        long expired =
                certifications.stream()
                        .filter(cert ->
                                cert.getStatus()
                                        == Certification.Status.EXPIRED)
                        .count();


        long pendingRenewal =
                certifications.stream()
                        .filter(cert ->
                                cert.getStatus()
                                        == Certification.Status.PENDING_RENEWAL)
                        .count();


        LocalDate today =
                LocalDate.now();

        LocalDate endDate =
                today.plusDays(30);


        long expiring =
                certifications.stream()
                        .filter(cert ->
                                cert.getExpiryDate() != null
                                        && !cert.getExpiryDate()
                                        .isBefore(today)
                                        && !cert.getExpiryDate()
                                        .isAfter(endDate))
                        .count();


        double renewalRate =
                total == 0
                        ? 0
                        : ((double) valid / total) * 100;


        return CertificationReportResponse.builder()
                .total(total)
                .valid(valid)
                .expired(expired)
                .pendingRenewal(pendingRenewal)
                .expiringWithin30Days(expiring)
                .renewalRate(
                        Math.round(
                                renewalRate * 100.0)
                                / 100.0)
                .build();
    }
}
