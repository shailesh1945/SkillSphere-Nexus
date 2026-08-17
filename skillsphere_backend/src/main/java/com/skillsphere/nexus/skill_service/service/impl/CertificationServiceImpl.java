package com.skillsphere.nexus.skill_service.service.impl;

import com.skillsphere.nexus.skill_service.dto.request.CertificationRequest;
import com.skillsphere.nexus.skill_service.dto.response.CertificationResponse;
import com.skillsphere.nexus.skill_service.model.Certification;
import com.skillsphere.nexus.skill_service.model.Certification.Status;
import com.skillsphere.nexus.skill_service.model.Employee;
import com.skillsphere.nexus.skill_service.repository.CertificationRepository;
import com.skillsphere.nexus.skill_service.repository.EmployeeRepository;
import com.skillsphere.nexus.skill_service.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public CertificationResponse addCertification(CertificationRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Certification certification = Certification.builder()
                .certificationName(request.getCertificationName())
                .issuingOrganization(request.getIssuingOrganization())
                .issueDate(request.getIssueDate())
                .expiryDate(request.getExpiryDate())
                .credentialId(request.getCredentialId())
                .employee(employee)
                .status(calculateStatus(request.getExpiryDate()))
                .build();

        return mapToResponse(certificationRepository.save(certification));
    }

    @Override
    public CertificationResponse updateCertification(
            UUID certificationId,
            CertificationRequest request) {

        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new RuntimeException("Certification not found"));

        certification.setCertificationName(request.getCertificationName());
        certification.setIssuingOrganization(request.getIssuingOrganization());
        certification.setIssueDate(request.getIssueDate());
        certification.setExpiryDate(request.getExpiryDate());
        certification.setCredentialId(request.getCredentialId());
        certification.setStatus(calculateStatus(request.getExpiryDate()));

        return mapToResponse(certificationRepository.save(certification));
    }

    @Override
    public CertificationResponse getCertificationById(
            UUID certificationId) {

        Certification certification =
                certificationRepository.findById(certificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Certification not found"));

        return mapToResponse(certification);
    }

    @Override
    public List<CertificationResponse> getAllCertifications() {

        return certificationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteCertification(UUID certificationId) {

        Certification certification =
                certificationRepository.findById(certificationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Certification not found"));

        certificationRepository.delete(certification);
    }

    @Override
    public List<CertificationResponse> getEmployeeCertifications(
            UUID employeeId) {

        return certificationRepository.findByEmployeeEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CertificationResponse> getActiveCertifications() {

        return certificationRepository.findByStatusIgnoreCase(Status.VALID)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CertificationResponse> getExpiredCertifications() {

        return certificationRepository.findByExpiryDateBefore(LocalDate.now())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<CertificationResponse> getCertificationsExpiringWithin(
            int days) {

        return certificationRepository.findByExpiryDateBetween(
                        LocalDate.now(),
                        LocalDate.now().plusDays(days))
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CertificationResponse renewCertification(
            UUID certificationId,
            CertificationRequest request) {

        Certification certification = certificationRepository.findById(certificationId)
                .orElseThrow(() -> new RuntimeException("Certification not found"));

        certification.setIssueDate(request.getIssueDate());
        certification.setExpiryDate(request.getExpiryDate());
        certification.setCredentialId(request.getCredentialId());
        certification.setStatus(calculateStatus(request.getExpiryDate()));

        return mapToResponse(certificationRepository.save(certification));
    }


    // Helper Methods

    private Status calculateStatus(LocalDate expiryDate) {

        if (expiryDate.isBefore(LocalDate.now())) {
            return Status.EXPIRED;
        }

        if (expiryDate.isBefore(LocalDate.now().plusDays(30))) {
            return Status.PENDING_RENEWAL;
        }

        return Status.VALID;
    }

    private CertificationResponse mapToResponse(Certification certification) {

        return CertificationResponse.builder()
                .certificationId(certification.getCertificationId())
                .certificationName(certification.getCertificationName())
                .issuingOrganization(certification.getIssuingOrganization())
                .issueDate(certification.getIssueDate())
                .expiryDate(certification.getExpiryDate())
                .verificationStatus(certification.getStatus().name())
                .build();
    }
}